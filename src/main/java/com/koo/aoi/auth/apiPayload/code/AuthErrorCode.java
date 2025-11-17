package com.koo.aoi.auth.apiPayload.code;

import com.koo.aoi.global.apiPayload.code.AoiBaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements AoiBaseErrorCode {

    REFRESH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AUTH401_1", "Refresh Token이 요청에 포함되어 있지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH401_2", "유효하지 않거나 만료된 Refresh Token입니다. 재로그인이 필요합니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH401_3", "토큰에 해당하는 사용자를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_4", "인증되지 않은 사용자입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
