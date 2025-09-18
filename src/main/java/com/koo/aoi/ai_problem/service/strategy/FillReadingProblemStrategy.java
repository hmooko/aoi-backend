package com.koo.aoi.ai_problem.service.strategy;

import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FillReadingProblemStrategy implements ProblemCreationStrategy {

    @Override
    public ProblemType getProblemType() {
        // 이 전략은 FILL_READING 유형을 처리
        return ProblemType.FILL_READING;
    }

    @Override
    public GeminiRequest createRequest(AIProblemCreateRequestDto requestDto) {
        String promptText = createPrompt(requestDto.getTargets());

        List<GeminiRequest.Part> parts = List.of(new GeminiRequest.Part(promptText));
        List<GeminiRequest.Content> contents = List.of(new GeminiRequest.Content(parts));

        // "읽기 찾기" 문제에 맞는 응답 스키마를 정의
        GeminiRequest.GeminiResponseSchema schema = new GeminiAIProblemResponseSchema(
                "이 문제의 대상이 된 한자 문자입니다.",
                "한자가 포함된 전체 단어",
                "발음의 일부가 빈칸(___)으로 처리된 문제",
                "정답 1개와 오답 3개를 포함한 4개의 선택지 발음(히라가나) 목록",
                "options 중 정답에 해당하는 발음"
        ).generateSchema();

        return new GeminiRequest(
                contents,
                new GeminiRequest.GenerationConfig("application/json", schema)
        );
    }

    private String createPrompt(List<String> kanjiList) {
        return String.format(
                """
                [역할]
                당신은 일본어 교육 및 평가 콘텐츠 제작 전문가입니다.
        
                [지시사항]
                아래 **[한자 리스트]**에 명시된 각 한자에 대해, 이어지는 **[규칙]**과 **[JSON 스키마]**에 맞춰 하나씩의 객관식 퀴즈 문제를 생성하여 전체 퀴즈를 구성해야 합니다.
        
                [한자 리스트]
                %s
        
                [규칙]
        
                1. 위에 명시된 [한자 리스트]의 순서에 맞춰 퀴즈 문제의 순서를 구성합니다. (첫 번째 한자로 첫 번째 문제, 두 번째 한자로 두 번째 문제 생성)
        
                2. 각 문제 객체 안에는 kanji 프로퍼티를 포함하여, 해당 문제가 어떤 한자를 기반으로 만들어졌는지 명시해야 합니다.
        
                3. 문제는 주어진 한자가 포함된, 교육적으로 의미 있고 사용 빈도가 높은 일본어 단어를 기반으로 생성합니다.
        
                4. 단어의 발음 중, 해당 문제의 kanji에 해당하는 부분을 빈칸(___)으로 만들어 문제를 출제합니다.
        
                5. 정답 선택지 1개와, 그럴듯한 오답 선택지 3개를 구성하여 총 4개의 선택지를 options 배열에 문자열로 담습니다.
        
                6. 정답에 해당하는 발음은 answer 필드에 별도로 명시합니다.
        
                [예시]
                {
                  "targetKanji": "語"
                  "targetWord": "日本語",
                  "question": "にほん(___)",
                  "options": [
                    "ご",
                    "ごう",
                    "か",
                    "が"
                  ],
                  "answer": "ご"
                }
                """,
                kanjiList
        );
    }
}
