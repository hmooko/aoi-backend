package com.koo.aoi.auth.apiPayload.exception;

import com.koo.aoi.global.apiPayload.AoiApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AoiApiResponse<Void>> handleException(
            AuthException ex
    ) {

        return ResponseEntity.status(ex.getCode().getStatus())
                .body(AoiApiResponse.onFailure(
                                ex.getCode(),
                                null
                        )
                );
    }
}
