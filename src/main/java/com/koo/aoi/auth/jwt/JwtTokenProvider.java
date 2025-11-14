package com.koo.aoi.auth.jwt;

import com.koo.aoi.user.domain.AoiUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final SecretKey signingKey;

    // 액세스 토큰 유효시간: 15분
    @Getter
    private final long accessTokenValidityInMilliseconds = 15 * 60 * 1000L;
    // 리프레시 토큰 유효시간: 7일
    @Getter
    private final long refreshTokenValidityInMilliseconds = 7 * 24 * 60 * 60 * 1000L;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateAccessToken(AoiUser user) {
        return generateToken(user, accessTokenValidityInMilliseconds);
    }

    public String generateRefreshToken(AoiUser user) {
        return generateToken(user, refreshTokenValidityInMilliseconds);
    }

    private String generateToken(AoiUser user, Long validity) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(validity);

        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // User 객체에서 직접 email 사용
                .claim("role", user.getRole().toString()) // User 객체에서 직접 role 사용
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}