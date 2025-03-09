package ru.hits.core.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.UUID;
import javax.crypto.SecretKey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getUserId(String token) {
        try {
            Claims claims = extractAllClaims(token.substring(7));
            return UUID.fromString(
                    (String) claims.get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Произошла ошибка в обработке токена");
        }
    }

//    public RoleEnum getUserRole(String token) {
//        try {
//            Claims claims = extractAllClaims(token.substring(7));
//            return RoleEnum.fromValue((String) claims.get("Role"));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException("Произошла ошибка в обработке токена");
//        }
//    }

}