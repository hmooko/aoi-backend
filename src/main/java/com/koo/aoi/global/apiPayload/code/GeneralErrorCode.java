package com.koo.aoi.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements AoiBaseErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 처리 중 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
