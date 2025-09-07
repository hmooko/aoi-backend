package com.koo.aoi.dto.problem;

import com.koo.aoi.domain.AIProblem;
import com.koo.aoi.domain.AIProblem.ProblemType;
import lombok.Getter;

import java.util.List;

@Getter
public class AIProblemRequestDto {
    private final Long id;
    private final ProblemType problemType;
    private final String sentence;
    private final String targetKanji;
    private final List<String> options;
    private final String answer;
    private final String targetWord;
    private final Long userId;

    public AIProblemRequestDto(AIProblem problem) {
        this.id = problem.getId();
        this.problemType = problem.getProblemType();
        this.sentence = problem.getSentence();
        this.targetKanji = problem.getTargetKanji();
        this.options = problem.getOptions();
        this.answer = problem.getAnswer();
        this.targetWord = problem.getTargetWord();
        this.userId = problem.getUser().getId();
    }
}
