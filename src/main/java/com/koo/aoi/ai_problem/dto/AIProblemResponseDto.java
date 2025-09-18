package com.koo.aoi.ai_problem.dto;

import com.koo.aoi.ai_problem.domain.AIProblem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AIProblemResponseDto {
    private Long id;
    private AIProblem.ProblemType problemType;
    private String sentence;
    private String targetKanji;
    private List<String> options;
    private String answer;
    private String targetWord;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public AIProblemResponseDto(AIProblem problem) {
        this.id = problem.getId();
        this.problemType = problem.getProblemType();
        this.sentence = problem.getSentence();
        this.targetKanji = problem.getTargetKanji();
        this.options = problem.getOptions();
        this.answer = problem.getAnswer();
        this.targetWord = problem.getTargetWord();
        this.createAt = problem.getCreateAt();
        this.updateAt = problem.getUpdateAt();
    }
}
