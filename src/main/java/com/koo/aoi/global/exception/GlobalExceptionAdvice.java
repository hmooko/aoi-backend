package com.koo.aoi.global.exception;

import com.koo.aoi.global.apiPayload.AoiApiResponse;
import com.koo.aoi.global.apiPayload.code.GeneralErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AoiApiResponse<String>> handleException(
            Exception ex
    ) {

        GeneralErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(code.getStatus())
                .body(AoiApiResponse.onFailure(
                                code,
                                ex.getMessage()
                        )
                );
    }
}
