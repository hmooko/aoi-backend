package com.koo.aoi.ai_problem.service.strategy;

import com.koo.aoi.gemini.dto.GeminiRequest;

import java.util.List;
import java.util.Map;


public class GeminiAIProblemResponseSchema {
    private final String targetKanjiDescription;
    private final String targetWordDescription;
    private final String sentenceDescription;
    private final String optionsDescription;
    private final String answerDescription;


    public GeminiAIProblemResponseSchema(
            String targetKanjiDescription,
            String targetWordDescription,
            String sentenceDescription,
            String optionsDescription,
            String answerDescription
    ) {
        this.targetKanjiDescription = targetKanjiDescription;
        this.targetWordDescription = targetWordDescription;
        this.sentenceDescription = sentenceDescription;
        this.optionsDescription = optionsDescription;
        this.answerDescription = answerDescription;
    }

    public GeminiRequest.GeminiResponseSchema generateSchema() {
        return GeminiRequest.GeminiResponseSchema.builder()
                .type("array")
                .description("생성된 한자 퀴즈 문제의 목록입니다.")
                .items(GeminiRequest.GeminiResponseSchema.builder()
                        .type("object")
                        .description("한자 퀴즈 문제 하나를 나타내는 구조입니다.")
                        .properties(Map.of(
                                "targetKanji", GeminiRequest.GeminiResponseSchema.builder()
                                        .type("string")
                                        .description(targetKanjiDescription)
                                        .build(),
                                "targetWord", GeminiRequest.GeminiResponseSchema.builder()
                                        .type("string")
                                        .description(targetWordDescription)
                                        .build(),
                                "sentence", GeminiRequest.GeminiResponseSchema.builder()
                                        .type("string")
                                        .description(sentenceDescription)
                                        .build(),
                                "options", GeminiRequest.GeminiResponseSchema.builder()
                                        .type("array")
                                        .description(optionsDescription)
                                        .items(GeminiRequest.GeminiResponseSchema.builder()
                                                .type("string")
                                                .description("선택지 문자열")
                                                .build())
                                        .build(),
                                "answer", GeminiRequest.GeminiResponseSchema.builder()
                                        .type("string")
                                        .description(answerDescription)
                                        .build()
                        ))
                        .required(List.of("targetKanji", "targetWord", "sentence", "options", "answer"))
                        .build()
                )
                .build();
    }
}
