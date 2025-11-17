package com.koo.aoi.auth.apiPayload.exception;

import com.koo.aoi.auth.apiPayload.code.AuthErrorCode;
import com.koo.aoi.global.apiPayload.code.AoiBaseErrorCode;
import com.koo.aoi.global.exception.AoiException;

public class AuthException extends AoiException {

    public AuthException(AuthErrorCode code) {
        super(code);
    }
}
