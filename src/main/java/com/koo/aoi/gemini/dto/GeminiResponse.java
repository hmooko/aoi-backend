package com.koo.aoi.gemini.dto;

import java.util.List;

/**
 * Gemini API 응답의 최상위 레벨 DTO 입니다.
 */
public record GeminiResponse(List<Candidate> candidates) {

    /**
     * 응답 후보
     */
    public record Candidate(Content content) {}

    /**
     * 응답 콘텐츠 블록
     */
    public record Content(List<Part> parts) {}

    /**
     * 응답 콘텐츠의 일부 (텍스트)
     */
    public record Part(String text) {}
}
