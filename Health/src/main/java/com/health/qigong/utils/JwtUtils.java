package com.health.qigong.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static java.security.KeyRep.Type.SECRET;
import static javax.crypto.Cipher.SECRET_KEY;

public class JwtUtils {
    private static  final String SECRET = "qigonghealthloginjwtsecret123456";
    private static  long EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;

    public static String generateToken(long userId){
        return Jwts.builder()
                .claim("userId",userId)
                .setIssuedAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public static Long parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();

        Object userId = claims.get("userId");
        return Long.valueOf(userId.toString());
    }
}
