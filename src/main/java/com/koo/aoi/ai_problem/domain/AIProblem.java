package com.koo.aoi.ai_problem.domain;

import com.koo.aoi.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIProblem {
    private ProblemType problemType;

    private String sentence;

    private String targetKanji;

    private List<String> options;

    private String answer;

    private String targetWord;

    public enum ProblemType {
        FIND_READING, FIND_KANJI, FILL_READING
    }
}
