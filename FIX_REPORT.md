# FIX_REPORT: 403 Forbidden on protected user APIs

> **Verification status, read first.** The sandbox used to prepare this fix has no `javac`, no Maven dependency cache,
> no network and no MySQL. **Nothing was compiled or executed.** The root cause below is established by reading the
> actual code path (strong evidence, but not a runtime reproduction), and the edits were reviewed line-by-line against
> the originals. Please build and run the checks in section 6 and tell me the result.

## 1. Root cause of the 403
`GET /api/v1/users/**` with a valid `Authorization: Bearer <accessToken>` ends up **unauthenticated**, and Spring Security answers unauthenticated requests with 403 when no entry point is configured.

Why it is unauthenticated:
1. `JwtAuthenticationFilter` parses the token, then calls `CustomUserDetailService.loadUserByUsername(email)`.
2. That method does `user.getRoles().stream()...`. `User.roles` is `@ManyToMany` = **LAZY** by default.
3. `CustomUserDetailService` had no `@Transactional`, and a servlet filter runs **before** Spring MVC's open-session-in-view
   interceptor, so there is no Hibernate session: `LazyInitializationException`.
4. The filter's `catch (Exception e) { SecurityContextHolder.clearContext(); }` **swallowed it silently** and continued
   as anonymous, so 403.

Why login still worked: `/api/v1/auth/login` runs inside a controller, where the open-in-view session exists, so the same lazy read succeeds there.
(Registered users always get `ROLE_GUEST`, so the roles collection is never empty and always hits this path.)

## 2. Files changed (all under `src/main/java/com/substring/auth/app/`)
| File | Change | Why |
|---|---|---|
| `auth/services/impl/CustomUserDetailService.java` | `@Transactional(readOnly = true)` on `loadUserByUsername` (+ import) | **Fixes the 403**: roles are read inside a session |
| `auth/config/JwtAuthenticationFilter.java` | Authenticate only if `jwtService.isAccessToken(token)`; `log.warn` (class + message, never the token) in the catch | A **refresh token could previously be used as an access token**; failures are no longer invisible |
| `auth/config/SecurityConfig.java` | `.exceptionHandling(... HttpStatusEntryPoint(401))` | Missing/invalid/expired token now returns **401** instead of a misleading 403 (authenticated-but-forbidden stays 403) |
| `auth/controllers/AuthController.java` | `refresh`: the 3 `RuntimeException`s become `JwtException` (+ import) | Invalid/unknown/revoked refresh token returned an unhandled **500** |
| `exceptions/GlobalExceptionHandler.java` | New `JwtException` handler returning 401 `ApiError` | Pairs with the line above (also covers expired/malformed/bad-signature refresh tokens) |
| `auth/services/impl/UserServiceImpl.java` | `updateUser`: password is now `passwordEncoder.encode(...)`; removed `setEnabled(userDto.isEnabled())` | PUT stored the password **in plaintext**, and, because `enabled` is READ_ONLY in JSON (always `false` on input), **disabled the account** on every update |
| `auth/controllers/UserController.java` | `createUser` (POST `/api/v1/users`): encode password before calling the service | That endpoint stored the password in plaintext (registration already encoded it) |
| `src/main/resources/application-{dev,qa,prod}.yml` | All credentials (DB user/password, Google/GitHub client id+secret, JWT secret) replaced by `${ENV_VAR}` placeholders, including commented-out old copies | You asked for no real secrets in the ZIP. **Your real values are NOT in this ZIP**, see section 5 |
| `.env.example` (new), `FIX_REPORT.md` | Docs only | |

Not changed: JWT signing/claims, registration, login, refresh logic and contract, DB schema, dependencies, routes, DTO shapes.

## 3. Things I deliberately did NOT change (decisions for you)
- **Authorization policy is still "any authenticated user".** `GET/PUT/DELETE/POST /api/v1/users/**` is open to any logged-in user, including GUEST. The
  admin-only rules (`hasRole(ADMIN)`, `AppConstants.AUTH_ADMIN_URLS`) exist only as commented-out code. I did not enable them because
  (a) the intended policy isn't defined in the live code, and (b) **there is no way to create an ADMIN user**
  (`AuthBackend` creates the `ROLE_ADMIN` row but never an admin user), so enforcing it would lock everyone out. Recommended follow-up: seed an admin, then restrict list/update/delete to ADMIN or the resource owner.
- **No refresh-token rotation, no logout/revoke endpoint.** `refresh` returns the same refresh token; the `revoked` flag and `replacedByToken` are never written by any code, so "revoked" can only be set directly in the DB.
- **Refresh request format** is the raw token string as the body (`@RequestBody String`), not JSON. Paste it **without quotes** in Swagger.

## 4. Build and test results
- Static review of the full request path and a diff review of every edit. Done.
- Maven build, unit/integration tests, running the app, every HTTP check: **NOT RUN** (see the status box).
- The only existing test, `AuthAppBackendApplicationTests.contextLoads`, is a full `@SpringBootTest`: it needs MySQL **and** the env vars below, otherwise it fails. Use `-DskipTests` if you build without them.
- I did not add new tests: I could not run them and refused to ship unexecuted tests that might break your build.

## 5. Configuration (secrets are now environment variables)
Set these before running (IntelliJ: *Run > Edit Configurations > Environment variables*, or your shell). Names are in `.env.example`:
`DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (at least 32 bytes), `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET`.
`prod` additionally uses `DB_URL`. `dev`/`qa` keep `jdbc:mysql://localhost:3306/...` in the yml.
**Use the same values you had before**, especially `JWT_SECRET`, or existing tokens become invalid. If you'd rather not use env vars locally, keep your own local copies of the three `application-*.yml` instead of the ones in this ZIP.
Because you pasted your config to me earlier, consider rotating the JWT secret, OAuth client secrets, and DB password if they're used anywhere real.

## 6. Run and verify (exact steps)
```
export DB_USERNAME=... DB_PASSWORD=... JWT_SECRET=...   (plus the OAuth vars)
./mvnw clean spring-boot:run          # profile "dev" is active by default; Java 21; MySQL must be running
```
Stop any older instance on port 8080 first (`lsof -i :8080` / `netstat -ano | findstr :8080`).
Swagger: `http://localhost:8080/swagger-ui/index.html`

1. `POST /api/v1/auth/register` with a NEW email, name, password (>= 6 chars) → 200 and a user without password. Same email again → 400 "User already exists with this email".
2. `POST /api/v1/auth/login` → copy `accessToken` and `refreshToken`.
3. Swagger **Authorize** → paste the **accessToken only** (Swagger adds `Bearer`; if the Authorize dialog isn't offered, the Bearer scheme is not declared in OpenApiConfig/APIDocConfig; then use `curl -H "Authorization: Bearer <token>"`).
4. `GET /api/v1/users/email/{email}` → expected **200**. `GET /api/v1/users` → **200**. `GET /api/v1/users/{id}` with the `id` from login → **200**; non-UUID id → 400; unknown UUID → 404.
5. No token → expect **401**. Garbage/expired token → **401**. Paste the **refreshToken** instead of the access token → **401** (new behavior).
6. `POST /api/v1/auth/refresh`, body = raw refresh token (no quotes) → 200 with a new `accessToken`. Use it on step 4. Garbage token → 401; token whose DB row you set `revoked = 1` → 401.
7. `PUT /api/v1/users/{id}` with a new password, then log in with it → 200 and `enabled` is still `true`.

**If any protected call is still 403 or 401, look at the console for `JWT authentication failed: <ExceptionClass>: <message>`.** That line (new) names the exact cause; send it to me.

## 7. Not verified
Compilation, tests, startup, every endpoint behavior above, DB state after refresh/revoke, the Swagger "Authorize" setup, and OAuth2 login.
