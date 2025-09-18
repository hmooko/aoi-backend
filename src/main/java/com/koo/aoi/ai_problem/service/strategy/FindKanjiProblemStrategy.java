package com.koo.aoi.ai_problem.service.strategy;

import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindKanjiProblemStrategy implements ProblemCreationStrategy {

    @Override
    public ProblemType getProblemType() {
        // 이 전략은 FIND_KANJI 유형을 처리
        return ProblemType.FIND_KANJI;
    }

    @Override
    public GeminiRequest createRequest(AIProblemCreateRequestDto requestDto) {
        String promptText = createPrompt(requestDto.getTargets());

        List<GeminiRequest.Part> parts = List.of(new GeminiRequest.Part(promptText));
        List<GeminiRequest.Content> contents = List.of(new GeminiRequest.Content(parts));

        // "읽기 찾기" 문제에 맞는 응답 스키마를 정의
        GeminiRequest.GeminiResponseSchema schema = new GeminiAIProblemResponseSchema(
                "한자 리스트에 있던, 이 문제의 기반이 된 한자",
                "문제의 정답이 되는 단어의 발음 (히라가나)",
                "대상 단어가 포함된 일본어 예문",
                "정답을 포함한 4개의 한자 표기 선택지 목록",
                "options 중 정답에 해당하는 한자 표기 단어"
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
                당신은 일본어 교육 및 평가 콘텐츠 제작 전문가입니다. 특히, 주어진 한자를 활용해 적절한 단어를 만들고, 그 단어의 동음이의어 등을 이용해 학습자에게 도전이 될 만한 매력적인 오답을 만드는 데 매우 능숙합니다.
        
                [지시사항]
                아래 **[입력 정보]**에 명시된 [한자 리스트]를 사용하여, 다음 3단계 과정을 통해 객관식 퀴즈를 생성해야 합니다. 리스트에 있는 각 한자당 하나의 문제를 순서대로 만들어야 합니다.
        
                1단계: 단어 생성
                주어진 한자를 포함하는, 교육적 가치가 높은 일본어 단어를 하나 생성합니다. (예: 입력 한자가 場일 경우, 工場이라는 단어를 생성)
        
                2단계: 문제문 구성
                1단계에서 생성한 단어의 올바른 발음(targetWord)을 파악하고, 해당 발음이 포함된 자연스러운 일본어 예문(sentence_prompt)을 만듭니다.
        
                3단계: 선택지 구성
                1단계에서 생성한 단어를 정답(answer)으로 하여, 매우 그럴듯한 오답 3개를 포함한 총 4개의 선택지(options)를 구성합니다.
        
                [한자 리스트]
                %s
        
                [규칙]
        
                1. targetKanji와 answer의 역할 정의 (가장 중요):
                    * targetKanji 프로퍼티에는 반드시 **[한자 리스트]에 있던 원본 '단일 한자'**를 기입해야 합니다.
                    * answer 프로퍼티에는 **1단계에서 AI가 생성한 '정답 단어'**를 기입해야 합니다.
                    * (예: 입력 한자가 場이고 생성 단어가 工場이면, targetKanji는 場이 되고 answer는 工場이 됩니다.)
        
                2. 난이도 일치: 생성하는 단어와 **예문(sentence_prompt)**의 전체적인 어휘 수준 및 문법 구조는, 입력된 [한자]의 일반적인 난이도와 비슷해야 합니다.
        
                3. 예문 생성 방식: 예문(sentence_prompt)에서 핵심 단어가 와야 할 자리에 해당 단어의 발음인 targetWord 값을 직접 채워 넣어야 합니다.
                
                4. 예문에서 targetWord는 괄호로 감싸야 합니다.
        
                4. 오답 생성 전략: 오답은 아래 전략을 적극적으로 활용합니다.
                    * 동음이의어: targetWord와 발음이 같은 다른 단어.
                    * 유의어/반의어: 의미가 비슷하거나 반대되는 단어.
                    * 유사 형태 한자: 모양이 비슷한 한자를 사용한 단어.
        
                5. 최종 출력되는 문제의 순서는 [한자 리스트]의 순서와 정확히 일치해야 합니다.
        
                6. JSON 형식 규칙을 엄격히 준수하며, 모든 텍스트는 일본어로 작성합니다.
                
                [예시]
                {
                  "targetKanji": "場",
                  "targetWord": "こうじょう",
                  "sentence": "この(こうじょう)ではくつを作っています。",
                  "options": [
                    "工事",
                    "公事",
                    "工場",
                    "広場"
                  ],
                  "answer": "工場"
                }
                """,
                kanjiList
        );
    }
}
