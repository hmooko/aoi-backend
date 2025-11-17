package com.koo.aoi.global.exception;

import com.koo.aoi.global.apiPayload.code.AoiBaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AoiException extends RuntimeException {
    private final AoiBaseErrorCode code;
}
