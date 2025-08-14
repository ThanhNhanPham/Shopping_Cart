package com.ecom.Shopping_Cart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.access-exp}") private long accessExpSec;
    @Value("${app.jwt.refresh-exp}") private long refreshExpSec;
    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails user) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpSec * 1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return Jwts.builder()
                .setId(jti).setSubject(user.getUsername())
                .setIssuedAt(now).setExpiration(exp)
                .addClaims(claims)
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }
    public String generateRefreshToken(UserDetails user) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpSec * 1000);
        return Jwts.builder()
                .setId(jti).setSubject(user.getUsername())
                .setIssuedAt(now).setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }
    public String extractJti(String token) {
        return parse(token).getBody().getId();
    }

    public Date extractExpiration(String token) {
        return parse(token).getBody().getExpiration();
    }

    public boolean isValid(String token, UserDetails user) {
        var b = parse(token).getBody();
        return user.getUsername().equals(b.getSubject()) && b.getExpiration().after(new Date());
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
    }

    public Instant toInstant(Date d) {
        return d.toInstant();
    }


}
