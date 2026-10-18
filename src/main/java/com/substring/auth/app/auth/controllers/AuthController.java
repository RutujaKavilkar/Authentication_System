//package com.substring.auth.app.auth.controllers;
//
//import com.substring.auth.app.auth.payload.LoginRequest;
//import com.substring.auth.app.auth.payload.RefreshTokenRequest;
//import com.substring.auth.app.auth.payload.TokenResponse;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.entities.RefreshToken;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.auth.repositories.RefreshTokenRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.impl.CookieService;
//import com.substring.auth.app.auth.services.impl.JwtService;
//import com.substring.auth.app.auth.services.AuthService;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@AllArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//    private final ModelMapper mapper;
//    private final CookieService cookieService;
//
//
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        // authenticate
//        Authentication authenticate = authenticate(loginRequest);
//       // User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new BadCredentialsException("Invalid Username or Password"));
//        User user = userRepository.findByEmail(loginRequest.getEmail());
//        if (!user.isEnable()) {
//            throw new DisabledException("User is disabled");
//
//        }
//
//        String jti = UUID.randomUUID().toString();
//
//        var refreshTokenOb = RefreshToken.builder()
//                .jti(jti)
//                .user(user)
//                .createdAt(Instant.now())
//                .expiresAt(
//                        Instant.now()
//                                .plusSeconds(jwtService.getRefreshTtlSeconds())
//                )
//                .revoked(false)
//                .build();
//
//        //access token--generate
//
//        refreshTokenRepository.save(refreshTokenOb);
//
//        String accessToken =
//                jwtService.generateAccessToken(user);
//
//        String refreshToken =
//                jwtService.generateRefreshToken(
//                        user,
//                        refreshTokenOb.getJti()
//                );
//
//
//
//        // use cookie service to attach refresh token in cookie
//
//        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
//        cookieService.addNoStoreHeaders(response);
//
////        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), mapper.map(user, UserDto.class));
//        TokenResponse tokenResponse =
//                TokenResponse.of(
//                        accessToken,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapper.map(user, UserDto.class)
//                );
//        return ResponseEntity.ok(tokenResponse);
//
//    }
//
//    private Authentication authenticate(LoginRequest loginRequest) {
//        try {
//
//            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
//
//        } catch (Exception e) {
//            throw new BadCredentialsException("Invalid Username or Password !!");
//        }
//    }
//
//
//    //access and refresh token renew karne lie lie api
//
//
//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refreshToken(
//            @RequestBody(required = false) RefreshTokenRequest body,
//            HttpServletResponse response,
//            HttpServletRequest request
//    ) throws InterruptedException {
//
//
//        //Thread.sleep(5000);
//
//        String refreshToken = readRefreshTokenFromRequest(body, request).orElseThrow(() -> new BadCredentialsException("Refresh token is missing"));
//
//
//        if(!jwtService.isRefreshToken(refreshToken)){
//            throw new BadCredentialsException("Invalid Refresh Token Type");
//        }
//
//        String jti = jwtService.getJti(refreshToken);
//        UUID userId =
//                UUID.fromString(
//                        jwtService.getUserId(refreshToken)
//                );
//        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Refresh token not recognized"));
//
//        if(storedRefreshToken.isRevoked()){
//            throw new BadCredentialsException("Refresh token expired or revoked");
//        }
//
//        if(storedRefreshToken.getExpiresAt().isBefore(Instant.now())){
//            throw new BadCredentialsException("Refresh token expired");
//        }
//
//        if(!storedRefreshToken.getUser().getId().equals(userId)){
//            throw new BadCredentialsException("Refresh token does not belong to this user");
//        }
//
//        //refresh token ko rotate:
//        storedRefreshToken.setRevoked(true);
//        String newJti= UUID.randomUUID().toString();
//        storedRefreshToken.setReplacedByToken(newJti);
//        refreshTokenRepository.save(storedRefreshToken);
//
//        User user = storedRefreshToken.getUser();
//
//        var newRefreshTokenOb = RefreshToken.builder()
//                .jti(newJti)
//                .user(user)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
//                .revoked(false)
//                .build();
//
//        refreshTokenRepository.save(newRefreshTokenOb);
//        String newAccessToken= jwtService.generateAccessToken(user);
//        String newRefreshToken = jwtService.generateRefreshToken(user, newRefreshTokenOb.getJti());
//
//
//        cookieService.attachRefreshCookie(response, newRefreshToken, (int) jwtService.getRefreshTtlSeconds());
//        cookieService.addNoStoreHeaders(response);
//        return ResponseEntity.ok(TokenResponse.of(newAccessToken, newRefreshToken, jwtService.getAccessTtlSeconds(), mapper.map(user, UserDto.class)));
//
//
//
//
//
//
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
//        readRefreshTokenFromRequest(null, request).ifPresent(token -> {
//            try {
//                if (jwtService.isRefreshToken(token)) {
//                    String jti = jwtService.getJti(token);
//                    refreshTokenRepository.findByJti(jti).ifPresent(rt -> {
//                        rt.setRevoked(true);
//                        refreshTokenRepository.save(rt);
//                    });
//                }
//            } catch (JwtException ignored) {
//            }
//        });
//
//        // Use CookieUtil (same behavior)
//        cookieService.clearRefreshCookie(response);
//        cookieService.addNoStoreHeaders(response);
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//
//    //this method will read refresh token from request header or body.
//    private Optional<String> readRefreshTokenFromRequest(RefreshTokenRequest body, HttpServletRequest request) {
////            1. prefer reading refresh token from cookie
//        if (request.getCookies() != null) {
//
//            Optional<String> fromCookie = Arrays.stream(request.getCookies())
//                    .filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
//                    .map(Cookie::getValue)
//                    .filter(v -> !v.isBlank())
//                    .findFirst();
//
//            if (fromCookie.isPresent()) {
//                return fromCookie;
//            }
//
//
//        }
//
//        // 2 body:
//        if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
//            return Optional.of(body.refreshToken());
//        }
//
//        //3. custom header
//        String refreshHeader = request.getHeader("X-Refresh-Token");
//        if (refreshHeader != null && !refreshHeader.isBlank()) {
//            return Optional.of(refreshHeader.trim());
//        }
//
//        //Authorization = Bearer <token>
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
//            String candidate = authHeader.substring(7).trim();
//            if (!candidate.isEmpty()) {
//                try {
//                    if (jwtService.isRefreshToken(candidate)) {
//                        return Optional.of(candidate);
//                    }
//                } catch (Exception ignored) {
//                }
//            }
//        }
//
//        return Optional.empty();
//
//
//    }
//
//
//////    @PostMapping("/register")
//////    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
//////        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
//////    }
////
////
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
//        System.out.println("REGISTER API HIT");
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(authService.registerUser(userDto));
//    }
//}
//

//
//package com.substring.auth.app.auth.controllers;
//
//import com.substring.auth.app.auth.entities.RefreshToken;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.auth.payload.*;
//import com.substring.auth.app.auth.repositories.RefreshTokenRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.AuthService;
//import com.substring.auth.app.auth.services.impl.JwtService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    // ✅ REGISTER
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@RequestBody UserDto dto) {
//        return ResponseEntity.ok(authService.registerUser(dto));
//    }
//
//    // ✅ LOGIN
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String accessToken = jwtService.generateAccessToken(user);
//
//        String jti = UUID.randomUUID().toString();
//
//        String refreshToken = jwtService.generateRefreshToken(user, jti);
//
//        RefreshToken rt = RefreshToken.builder()
//                .jti(jti)
//                .user(user)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
//                .revoked(false)
//                .build();
//
//        refreshTokenRepository.save(rt);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        accessToken,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    // ✅ REFRESH TOKEN
//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
//
//        if (!jwtService.isRefreshToken(refreshToken)) {
//            throw new RuntimeException("Invalid Refresh Token");
//        }
//
//        String jti = jwtService.getJti(refreshToken);
//
//        RefreshToken stored = refreshTokenRepository.findByJti(jti)
//                .orElseThrow(() -> new RuntimeException("Token not found"));
//
//        if (stored.isRevoked()) {
//            throw new RuntimeException("Token revoked");
//        }
//
//        User user = stored.getUser();
//
//        String newAccess = jwtService.generateAccessToken(user);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        newAccess,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    // ✅ MAPPER (NO MODELMAPPER NEEDED)
//    private UserDto mapToDto(User user) {
//        UserDto dto = new UserDto();
//        dto.setName(user.getName());
//        dto.setEmail(user.getEmail());
//        dto.setImage(user.getImage());
//        dto.setEnabled(user.isEnabled());
//        return dto;
//    }
//}


//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    // ✅ REGISTER
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@RequestBody UserDto dto) {
//        System.out.println("REGISTER API HIT");
//        return ResponseEntity.ok(authService.registerUser(dto));
//    }
//
//    // ✅ LOGIN
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String accessToken = jwtService.generateAccessToken(user);
//
//        String jti = UUID.randomUUID().toString();
//
//        String refreshToken = jwtService.generateRefreshToken(user, jti);
//
//        RefreshToken rt = RefreshToken.builder()
//                .jti(jti)
//                .user(user)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
//                .revoked(false)
//                .build();
//
//        refreshTokenRepository.save(rt);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        accessToken,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    // ✅ REFRESH
//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
//
//        String jti = jwtService.getJti(refreshToken);
//
//        RefreshToken stored = refreshTokenRepository.findByJti(jti)
//                .orElseThrow(() -> new RuntimeException("Token not found"));
//
//        if (stored.isRevoked()) {
//            throw new RuntimeException("Token revoked");
//        }
//
//        User user = stored.getUser();
//
//        String newAccess = jwtService.generateAccessToken(user);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        newAccess,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    private UserDto mapToDto(User user) {
//        UserDto dto = new UserDto();
//        dto.setName(user.getName());
//        dto.setEmail(user.getEmail());
//        dto.setImage(user.getImage());
//        dto.setEnabled(user.isEnabled());
//        return dto;
//    }
//}
//
//
//package com.substring.auth.app.auth.controllers;
//
//import com.substring.auth.app.auth.entities.RefreshToken;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.auth.payload.LoginRequest;
//import com.substring.auth.app.auth.payload.TokenResponse;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.repositories.RefreshTokenRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.AuthService;
//import com.substring.auth.app.auth.services.impl.JwtService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    // REGISTER
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto dto) {
//        return ResponseEntity.ok(authService.registerUser(userDto));
//    }
//
//    // LOGIN
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String accessToken = jwtService.generateAccessToken(user);
//
//        String jti = UUID.randomUUID().toString();
//
//        String refreshToken = jwtService.generateRefreshToken(user, jti);
//
//        RefreshToken rt = RefreshToken.builder()
//                .jti(jti)
//                .user(user)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
//                .revoked(false)
//                .build();
//
//        refreshTokenRepository.save(rt);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        accessToken,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    // REFRESH
//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
//
//        if (!jwtService.isRefreshToken(refreshToken)) {
//            throw new RuntimeException("Invalid Refresh Token");
//        }
//
//        String jti = jwtService.getJti(refreshToken);
//
//        RefreshToken stored = refreshTokenRepository.findByJti(jti)
//                .orElseThrow(() -> new RuntimeException("Token not found"));
//
//        if (stored.isRevoked()) {
//            throw new RuntimeException("Token revoked");
//        }
//
//        User user = stored.getUser();
//
//        String newAccess = jwtService.generateAccessToken(user);
//
//        return ResponseEntity.ok(
//                TokenResponse.of(
//                        newAccess,
//                        refreshToken,
//                        jwtService.getAccessTtlSeconds(),
//                        mapToDto(user)
//                )
//        );
//    }
//
//    private UserDto mapToDto(User user) {
//        UserDto dto = new UserDto();
//        dto.setName(user.getName());
//        dto.setEmail(user.getEmail());
//        dto.setImage(user.getImage());
//        dto.setEnabled(user.isEnabled());
//        return dto;
//    }
//}
//
//package com.substring.auth.app.auth.controllers;
//
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.services.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//@RestController
////@RequestMapping("/api/v1/auth")
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto dto) {
//        return ResponseEntity.ok(authService.registerUser(dto));
//    }
//}

package com.substring.auth.app.auth.controllers;

import com.substring.auth.app.auth.entities.RefreshToken;
import com.substring.auth.app.auth.entities.User;
import com.substring.auth.app.auth.payload.*;
import com.substring.auth.app.auth.repositories.RefreshTokenRepository;
import com.substring.auth.app.auth.repositories.UserRepository;
import com.substring.auth.app.auth.services.AuthService;
import com.substring.auth.app.auth.services.impl.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ✅ ADD THESE DEPENDENCIES
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(authService.registerUser(dto));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        // authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // tokens
        String accessToken = jwtService.generateAccessToken(user);

        String jti = UUID.randomUUID().toString();
        String refreshToken = jwtService.generateRefreshToken(user, jti);

        // save refresh token
        RefreshToken rt = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(rt);

        return ResponseEntity.ok(
                TokenResponse.of(
                        accessToken,
                        refreshToken,
                        jwtService.getAccessTtlSeconds(),
                        mapToDto(user)
                )
        );
    }

    // ================= REFRESH =================
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new JwtException("Invalid Refresh Token");
        }

        String jti = jwtService.getJti(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new JwtException("Token not found"));

        if (stored.isRevoked()) {
            throw new JwtException("Token revoked");
        }

        User user = stored.getUser();

        String newAccess = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(
                TokenResponse.of(
                        newAccess,
                        refreshToken,
                        jwtService.getAccessTtlSeconds(),
                        mapToDto(user)
                )
        );
    }

    // helper
    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId().toString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setImage(user.getImage());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}

