package com.koo.aoi.ai_problem.apiPayload.code;

import com.koo.aoi.global.apiPayload.code.AoiBaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiProblemSuccessCode implements AoiBaseErrorCode {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
