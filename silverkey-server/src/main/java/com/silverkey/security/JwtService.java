package com.silverkey.security;

import com.silverkey.config.JwtConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtService(JwtConfiguration configuration) {

        this.secretKey = Keys.hmacShaKeyFor(
                configuration.getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
//        hmac based signing
//        The JWT gets cryptographically signed.
//        Think of it like a tamper-proof seal.
//        If an attacker changes something inside the token, the signature no longer matches.

        this.expirationMillis =
                configuration.getExpirationMinutes() * 60 * 1000;
        //Because Java's Date works with milliseconds.
    }

    public String generateToken(UUID userId) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + expirationMillis
        );

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact(); //turns the JWT object into the actual string we'll send to Postman/client
    }

    public UUID extractUserId(String token) {

        String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return UUID.fromString(subject);
    }
}
