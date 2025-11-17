package com.koo.aoi.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements AoiBaseSuccessCode {
    // ====================== 200 OK ======================
    GET_SUCCESS(HttpStatus.OK, "COMMON20001", "데이터를 성공적으로 조회했습니다."),

    UPDATE_SUCCESS(HttpStatus.OK, "COMMON2002", "데이터가 성공적으로 업데이트되었습니다."),

    PROCESS_SUCCESS(HttpStatus.OK, "COMMON2003", "요청하신 작업이 성공적으로 처리되었습니다."),

    // ====================== 201 Created ======================
    RESOURCE_CREATED(HttpStatus.CREATED, "COMMON2011", "새로운 리소스가 성공적으로 생성되었습니다."),

    // ====================== 204 No Content ======================
    DELETE_SUCCESS(HttpStatus.NO_CONTENT, "COMMON2041", "데이터를 성공적으로 삭제했습니다."),

    NO_CONTENT_UPDATE(HttpStatus.NO_CONTENT, "COMMON2042", "데이터가 성공적으로 수정되었으나, 응답 본문은 없습니다.");
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
