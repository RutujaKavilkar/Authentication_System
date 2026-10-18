//package com.substring.auth.app.auth.config;
//
//import com.substring.auth.app.auth.services.impl.JwtService;
//import com.substring.auth.app.auth.helpers.UserHelper;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import io.jsonwebtoken.*;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//        logger.info("Authorization header : {}", header);
//
//        if (header != null && header.startsWith("Bearer ")) {
//
//
//            //token extract and validate then authentication create and then security context ke ander set karunga.
//
//            String token = header.substring(7);
//            //check for access token
//
//            try {
//
//                if (!jwtService.isAccessToken(token)) {
//                    //message pass kar hai---
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//
//
//                Jws<Claims> parse = jwtService.parse(token);
//
//
//                Claims payload = parse.getPayload();
//
//
//                String userId = payload.getSubject();
//                UUID userUuid = UserHelper.parseUUID(userId);
//
//                userRepository.findById(userUuid).ifPresent(user -> {
//
//                    //check for user enable or not
//
//                    if (user.isEnabled()) {
//                        // user mil chuka hai database se
//                        List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() : user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        //final line : to set the authentication to security context
//                        if (SecurityContextHolder.getContext().getAuthentication() == null)
//                            SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//
//
//                });
//
//
//            } catch (ExpiredJwtException e) {
//                request.setAttribute("error", "Token Expired");
//                // e.printStackTrace();
//
//            } catch (Exception e) {
//                request.setAttribute("error", "Invalid Token");
////                e.printStackTrace();
//
//            }
//
//
//        }
//
//        filterChain.doFilter(request, response);
//
//
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getRequestURI().startsWith("/api/v1/auth");
//    }
//}
//
//package com.substring.auth.app.auth.config;
//
//import com.substring.auth.app.auth.services.impl.JwtService;
//import com.substring.auth.app.auth.services.impl.CustomUserDetailService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        // ✅ Skip auth APIs
//        if (path.startsWith("/api/v1/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        String email = jwtService.getUserId(token); // now returns email
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            var userDetails = userDetailsService.loadUserByUsername(email);
//
//            var authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//            );
//
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
// Safe below
//
//package com.substring.auth.app.auth.config;
//
//import com.substring.auth.app.auth.services.impl.JwtService;
//import com.substring.auth.app.auth.services.impl.CustomUserDetailService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailService userDetailsService;
//
//    // ✅ 🔥 THIS IS THE REAL FIX
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getServletPath();
//
//        return path.startsWith("/api/v1/auth")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-ui");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        String email = jwtService.getUserId(token);
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            var userDetails = userDetailsService.loadUserByUsername(email);
//
//            var authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//            );
//
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
//package com.substring.auth.app.auth.config;
//
//import com.substring.auth.app.auth.services.impl.JwtService;
//import com.substring.auth.app.auth.services.impl.CustomUserDetailService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailService userDetailsService;
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getServletPath();
//
//        return path.startsWith("/api/v1/auth")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-ui");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        // No token → skip
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        String email = jwtService.getUserId(token);
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            var userDetails = userDetailsService.loadUserByUsername(email);
//
//            var authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    null,
//                    userDetails.getAuthorities()
//            );
//
//            authToken.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
//package com.substring.auth.app.auth.config;
//
//import com.substring.auth.app.auth.services.impl.CustomUserDetailService;
//import com.substring.auth.app.auth.services.impl.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final CustomUserDetailService userDetailsService;
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//
//        String path = request.getServletPath();
//
//        return path.startsWith("/api/v1/auth")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-ui")
//                || path.startsWith("/swagger-ui.html");
//    }
////
////    @Override
////    protected void doFilterInternal(
////            HttpServletRequest request,
////            HttpServletResponse response,
////            FilterChain filterChain
////    ) throws ServletException, IOException {
////
////        String authHeader = request.getHeader("Authorization");
////
////        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        String token = authHeader.substring(7);
////
////        try {
////            String email = jwtService.getUserId(token);
////
////            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////
////                var userDetails = userDetailsService.loadUserByUsername(email);
////
////                UsernamePasswordAuthenticationToken authToken =
////                        new UsernamePasswordAuthenticationToken(
////                                userDetails,
////                                null,
////                                userDetails.getAuthorities()
////                        );
////
////                authToken.setDetails(
////                        new WebAuthenticationDetailsSource().buildDetails(request)
////                );
////
////                SecurityContextHolder.getContext().setAuthentication(authToken);
////            }
////
////        } catch (Exception e) {
////            SecurityContextHolder.clearContext();
////        }
////
////        filterChain.doFilter(request, response);
////}
//@Override
//protected void doFilterInternal(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        FilterChain filterChain
//) throws ServletException, IOException {
//
//    String authHeader = request.getHeader("Authorization");
//
//    System.out.println("AUTH HEADER: " + authHeader); // DEBUG
//
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    String token = authHeader.substring(7);
//
//    try {
//        String email = jwtService.getUserId(token);
//        System.out.println("EMAIL FROM TOKEN: " + email); // DEBUG
//
//        if (email != null) {
//
//            var userDetails = userDetailsService.loadUserByUsername(email);
//
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//
//            authToken.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );
//
//            // 🔥 ALWAYS SET AUTH (NO CONDITION)
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        SecurityContextHolder.clearContext();
//    }
//
//    filterChain.doFilter(request, response);
//}
//}

package com.substring.auth.app.auth.config;

import com.substring.auth.app.auth.services.impl.CustomUserDetailService;
import com.substring.auth.app.auth.services.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/api/v1/auth")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-ui.html");
    }

//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        System.out.println("AUTH HEADER: " + authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            String email = jwtService.getUserId(token);
//            System.out.println("EMAIL: " + email);
//
//            if (email != null) {
//
//                var userDetails = userDetailsService.loadUserByUsername(email);
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//
//                // 🔥 FORCE AUTHENTICATION (NO CONDITIONS)
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            SecurityContextHolder.clearContext();
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.getUserId(token);

            if (email != null
                    && jwtService.isAccessToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                var userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            log.warn("JWT authentication failed: {}: {}", e.getClass().getSimpleName(), e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}