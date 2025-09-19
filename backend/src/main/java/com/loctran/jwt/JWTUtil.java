package com.loctran.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JWTUtil {

    private static final String SECRET_KEY = "loctran_123456789_loctran_123456789_loctran_123456789_loctran_123456789_loctran_123456789_loctran_123456789_loctran_123456789_loctran_123456789";

    public String issueToken(String subject){
        return issueToken(subject,Map.of());
    }

    public String issueToken(String subject, String ...scopes){
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(
            String subject,
            Map<String,Object> claims){
                return
                Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuer("https://loctran.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    public String getSubject(String token){
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token){
        return
                Jwts
                        .parser()
                        .verifyWith((SecretKey) getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
    }

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean isTokenValid(String jwt, String username){
        String subject = getSubject(jwt);

        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt){
        return getClaimsFromToken(jwt).getExpiration().before(
                Date.from(Instant.now())
        );
    }
}
