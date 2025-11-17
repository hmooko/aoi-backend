package com.koo.aoi.global.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface AoiBaseCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
