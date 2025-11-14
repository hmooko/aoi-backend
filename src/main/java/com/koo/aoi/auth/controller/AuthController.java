package com.koo.aoi.auth.controller;

import com.koo.aoi.auth.jwt.JwtTokenProvider;
import com.koo.aoi.auth.service.RefreshTokenService;
import com.koo.aoi.auth.util.CookieUtil;
import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.auth.dto.AuthTokenResponse;
import com.koo.aoi.auth.dto.UserSignInRequest;
import com.koo.aoi.auth.dto.UserSignUpRequest;
import com.koo.aoi.user.service.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "구글 로그인", description = "구글 OAuth2 로그인을 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "구글 로그인 페이지로 리다이렉트", content = @Content)
    })
    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @Operation(summary = "토큰 재발급", description = "Cookie에 담긴 Refresh Token을 사용하여 Access Token과 Refresh Token을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Refresh Token이 없거나 유효하지 않음", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(
            @Parameter(hidden = true) @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }

        try {
            List<String> tokenList = refreshTokenService.reissueToken(refreshToken);

            String newAccessToken = tokenList.get(0);
            String newRefreshToken = tokenList.get(1);

            cookieUtil.addCookie(response, "refresh_token", newRefreshToken, (int) (jwtTokenProvider.getRefreshTokenValidityInMilliseconds() / 1000L));

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (IllegalArgumentException | JwtException e) {
            // 실패 시 쿠키 삭제
            cookieUtil.deleteCookie(response, "refresh_token");
            // JwtException (만료, 변조 등), IllegalArgumentException (DB에 없음)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage()); // 혹은 에러 메시지
        }
    }
}
