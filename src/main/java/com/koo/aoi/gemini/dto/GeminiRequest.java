package com.koo.aoi.gemini.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * Gemini API 요청의 최상위 레벨 DTO 입니다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiRequest(
        List<Content> contents,
        @JsonProperty("generationConfig")
        GenerationConfig generationConfig
) {

    /**
     * 단일 콘텐츠 블록
     */
    public record Content(List<Part> parts) {}

    /**
     * 콘텐츠의 일부 (텍스트)
     */
    public record Part(String text) {}

    /**
     * 생성 관련 설정
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record GenerationConfig(
            @JsonProperty("responseMimeType")
            String responseMimeType,
            @JsonProperty("responseSchema")
            GeminiResponseSchema responseSchema
    ) {}

    /**
     * JSON 스키마를 정의하는 클래스
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GeminiResponseSchema {
        private final String type;
        private final String description;
        private final Map<String, GeminiResponseSchema> properties;
        private final GeminiResponseSchema items;
        private final List<String> required;

    }
}
