package com.koo.aoi.auth.service;

import com.koo.aoi.auth.domain.RefreshToken;
import com.koo.aoi.auth.jwt.JwtTokenProvider;
import com.koo.aoi.auth.repository.RefreshTokenRepository;
import com.koo.aoi.user.domain.AoiUser;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Refresh Token을 받아 Access Token과 Refresh Token을 재발급
     */
    @Transactional
    public List<String> reissueToken(String oldRefreshToken) {

        // 1. Refresh Token 유효성 검증 (JWT 라이브러리)
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtException("Invalid refresh token");
        }

        // 2. DB에 저장된 토큰과 일치하는지 확인
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in DB"));

        // 3. 새로운 Access Token 생성 (DB에 저장된 email 기반)
        AoiUser user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
        refreshToken.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return List.of(newAccessToken, newRefreshToken);
    }
}
