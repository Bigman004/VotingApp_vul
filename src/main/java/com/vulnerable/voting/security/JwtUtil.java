package com.vulnerable.voting.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private String secretKey = "";

    @PostConstruct
    public void init() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            this.secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("authority", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ (1000*60*60)))
                .signWith(getKey())
                .compact();
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }
    public String extractAuthority(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("authority").toString();
    }
    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        }
        catch (Exception e){
            log.error("jwt validation message {}", e.getMessage());
            return false;
        }
    }

    private Key getKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
    private Date extractExpiration(String token) {
       return Jwts.parser().verifyWith((SecretKey) getKey()).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration();
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }
}
