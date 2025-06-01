package com.example.assetrecordingapp.util;
import com.example.assetrecordingapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final ThreadLocal<Claims> CURRENT_CLAIMS = new ThreadLocal<>();
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Claims parseTokenOnce(String token) {
        Claims claims = CURRENT_CLAIMS.get();
        if (claims == null) {
            try {
                claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                CURRENT_CLAIMS.set(claims);
            } catch (Exception e) {
                return null;
            }
        }
        return claims;
    }

    public void clearCurrentClaims() {
        CURRENT_CLAIMS.remove();
    }

    public String generateToken(User user) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        return parseTokenOnce(token) != null;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseTokenOnce(token);
        return claims == null || claims.getExpiration().before(new Date());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseTokenOnce(token);
        return claims != null ? Long.parseLong(claims.getSubject()) : null;
    }
}