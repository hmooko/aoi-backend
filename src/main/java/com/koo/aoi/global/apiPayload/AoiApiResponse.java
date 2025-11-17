package com.koo.aoi.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.koo.aoi.global.apiPayload.code.AoiBaseErrorCode;
import com.koo.aoi.global.apiPayload.code.AoiBaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class AoiApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("result")
    private T result;

    // 성공한 경우 (result 포함)
    public static <T> AoiApiResponse<T> onSuccess(AoiBaseSuccessCode code, T result) {
        return new AoiApiResponse<>(true, code.getCode(), code.getMessage(), result);
    }

    // 성공한 경우 (result X)
    public static <T> AoiApiResponse<T> onSuccessWithoutResult(AoiBaseSuccessCode code) {
        return new AoiApiResponse<>(true, code.getCode(), code.getMessage(), null);
    }

    // 실패한 경우 (result 포함)
    public static <T> AoiApiResponse<T> onFailure(AoiBaseErrorCode code, T result) {
        return new AoiApiResponse<>(false, code.getCode(), code.getMessage(), result);
    }

    // 실패한 경우 (result X)
    public static <T> AoiApiResponse<T> onFailureWithoutResult(AoiBaseErrorCode code) {
        return new AoiApiResponse<>(true, code.getCode(), code.getMessage(), null);
    }
}
