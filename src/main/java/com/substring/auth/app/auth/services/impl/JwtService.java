package com.substring.auth.app.auth.services.impl;

import com.substring.auth.app.auth.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-ttl-seconds}")
    private long accessTokenExp;

    @Value("${security.jwt.refresh-ttl-seconds}")
    private long refreshTokenExp;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ACCESS TOKEN
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", "access")
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExp * 1000))
                .signWith(getKey())
                .compact();
    }

    // REFRESH TOKEN
    public String generateRefreshToken(User user, String jti) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", "refresh")
                .id(jti)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExp * 1000))
                .signWith(getKey())
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
    }

    public String getUserId(String token) {
        return parse(token)
                .getPayload()
                .getSubject();
    }

    public String getJti(String token) {
        return parse(token)
                .getPayload()
                .getId();
    }

    public boolean isAccessToken(String token) {
        return "access".equals(
                parse(token)
                        .getPayload()
                        .get("type")
        );
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(
                parse(token)
                        .getPayload()
                        .get("type")
        );
    }

    public long getAccessTtlSeconds() {
        return accessTokenExp;
    }

    public long getRefreshTtlSeconds() {
        return refreshTokenExp;
    }
}