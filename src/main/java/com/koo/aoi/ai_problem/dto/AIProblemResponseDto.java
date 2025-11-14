package com.koo.aoi.ai_problem.dto;

import com.koo.aoi.ai_problem.domain.AIProblem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "AI 문제 응답 DTO")
public class AIProblemResponseDto {
    @Schema(description = "문제 유형", example = "FIND_READING")
    private AIProblem.ProblemType problemType;
    @Schema(description = "문제 문장", example = "この漢字の読み方は何ですか？")
    private String sentence;
    @Schema(description = "대상 한자", example = "漢字")
    private String targetKanji;
    @Schema(description = "선택지", example = "[\"かんじ\", \"かんじ\", \"かんじ\"]")
    private List<String> options;
    @Schema(description = "정답", example = "かんじ")
    private String answer;
    @Schema(description = "대상 단어", example = "漢字")
    private String targetWord;

    public AIProblemResponseDto(AIProblem problem) {
        this.problemType = problem.getProblemType();
        this.sentence = problem.getSentence();
        this.targetKanji = problem.getTargetKanji();
        this.options = problem.getOptions();
        this.answer = problem.getAnswer();
        this.targetWord = problem.getTargetWord();
    }
}
