package com.paparazzi.antifakenews.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationSeconds;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.expiration-seconds}") long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(ensureBase64(secret)));
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private String ensureBase64(String s) {
        try {
            Decoders.BASE64.decode(s);
            return s;
        } catch (Exception e) {
            return java.util.Base64.getEncoder().encodeToString(s.getBytes());
        }
    }
}
