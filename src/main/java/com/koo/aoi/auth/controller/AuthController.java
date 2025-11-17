package com.koo.aoi.auth.controller;

import com.koo.aoi.auth.apiPayload.code.AuthErrorCode;
import com.koo.aoi.auth.apiPayload.code.AuthSuccessCode;
import com.koo.aoi.auth.apiPayload.exception.AuthException;
import com.koo.aoi.auth.jwt.JwtTokenProvider;
import com.koo.aoi.auth.service.RefreshTokenService;
import com.koo.aoi.auth.util.CookieUtil;
import com.koo.aoi.global.apiPayload.AoiApiResponse;
import com.koo.aoi.global.apiPayload.code.GeneralErrorCode;
import com.koo.aoi.global.apiPayload.code.GeneralSuccessCode;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/refresh")
    public AoiApiResponse<Map<String, String>> refreshTokens(
            @RequestBody String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_MISSING);
        }

        try {
            List<String> tokenList = refreshTokenService.reissueToken(refreshToken);

            String newAccessToken = tokenList.get(0);
            String newRefreshToken = tokenList.get(1);

            Map<String, String> responseBody = Map.of(
                    "access_token", newAccessToken,
                    "refresh_token", newRefreshToken
            );

            return AoiApiResponse.onSuccess(AuthSuccessCode.TOKEN_REFRESH_SUCCESS, responseBody);

        } catch (IllegalArgumentException | JwtException e) {
            // 실패 시 쿠키 삭제
            cookieUtil.deleteCookie(response, "refresh_token");
            // JwtException (만료, 변조 등), IllegalArgumentException (DB에 없음)
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }
    }
}
