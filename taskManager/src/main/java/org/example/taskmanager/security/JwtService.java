package org.example.taskmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String SECRET_KEY = "671491AE98362741F722202EED3288E8FF2508B35315ADBF75EEB3195A926B40";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractBody(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractBody(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractBody(token).getExpiration();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Date expiration = extractExpiration(token);
        return username.equals(userDetails.getUsername()) && expiration.after(new Date());
    }

    public Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
