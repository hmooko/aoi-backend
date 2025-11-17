package com.koo.aoi.ai_problem.dto;

import com.koo.aoi.ai_problem.domain.AIProblem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AIProblemResponseDto {
    private AIProblem.ProblemType problemType;
    private String sentence;
    private String targetKanji;
    private List<String> options;
    private String answer;
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
