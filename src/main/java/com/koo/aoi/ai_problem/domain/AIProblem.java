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
@Entity
public class AIProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum의 이름을 DB에 저장 (EnumType.ORDINAL은 순서를 저장하므로 비권장)
    @Column(nullable = false)
    private ProblemType problemType;

    @Column(nullable = false)
    private String sentence;

    @Column(nullable = false)
    private String targetKanji;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<String> options;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String targetWord;

    @Column(nullable = false,  updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public enum ProblemType {
        FIND_READING, FIND_KANJI, FILL_READING
    }
}
