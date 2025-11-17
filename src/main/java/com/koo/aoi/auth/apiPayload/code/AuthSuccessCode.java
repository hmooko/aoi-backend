package com.koo.aoi.auth.apiPayload.code;

import com.koo.aoi.global.apiPayload.code.AoiBaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements AoiBaseSuccessCode {

    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "S204_1", "액세스 토큰이 성공적으로 갱신되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "S204_2", "로그인이 성공적으로 완료되었습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
