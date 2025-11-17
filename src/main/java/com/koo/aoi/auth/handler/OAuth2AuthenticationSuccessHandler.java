package com.koo.aoi.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koo.aoi.auth.apiPayload.code.AuthSuccessCode;
import com.koo.aoi.auth.domain.RefreshToken;
import com.koo.aoi.auth.jwt.JwtTokenProvider;
import com.koo.aoi.auth.repository.RefreshTokenRepository;
import com.koo.aoi.auth.util.CookieUtil;
import com.koo.aoi.global.apiPayload.AoiApiResponse;
import com.koo.aoi.global.apiPayload.code.GeneralSuccessCode;
import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.user.domain.Provider;
import com.koo.aoi.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;
    private final UserService userService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String provider = (String) attributes.get("provider");

        AoiUser user = userService.findByEmailAndProvider(email, Provider.valueOf(provider));

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshTokenValue = jwtTokenProvider.generateRefreshToken(user);

        refreshTokenRepository.findById(user.getId())
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshToken.setRefreshToken(refreshTokenValue);
                            refreshTokenRepository.save(refreshToken);
                        },
                        () -> refreshTokenRepository.save(new RefreshToken(user, refreshTokenValue))
                );
//        쿠키로 refresh token 전달

//        cookieUtil.addCookie(response, "refresh_token", refreshTokenValue, (int) (jwtTokenProvider.getRefreshTokenValidityInMilliseconds() / 1000L));
//
//        log.info("Social login successful for user: {}", email);
//
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");

        Map<String, String> payload = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshTokenValue
        );

        AoiApiResponse<Map<String, String>> apiResponse = AoiApiResponse.onSuccess(AuthSuccessCode.LOGIN_SUCCESS, payload);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
