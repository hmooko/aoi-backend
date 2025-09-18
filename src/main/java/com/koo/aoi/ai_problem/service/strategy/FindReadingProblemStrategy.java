package com.koo.aoi.ai_problem.service.strategy;

import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.gemini.dto.GeminiRequest.GeminiResponseSchema;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // 스프링 빈으로 등록하여 나중에 Service에서 주입받을 수 있도록 함
public class FindReadingProblemStrategy implements ProblemCreationStrategy {

    @Override
    public ProblemType getProblemType() {
        // 이 전략은 FIND_READING 유형을 처리
        return ProblemType.FIND_READING;
    }

    @Override
    public GeminiRequest createRequest(AIProblemCreateRequestDto requestDto) {
        String promptText = createPrompt(requestDto.getTargets());

        List<GeminiRequest.Part> parts = List.of(new GeminiRequest.Part(promptText));
        List<GeminiRequest.Content> contents = List.of(new GeminiRequest.Content(parts));

        // "읽기 찾기" 문제에 맞는 응답 스키마를 정의
        GeminiResponseSchema schema = new GeminiAIProblemResponseSchema(
                "한자 리스트에 있던, 이 문제의 기반이 된 한자",
                "AI가 생성한, 이 문제의 대상이 되는 핵심 단어",
                "대상 단어가 포함된 일본어 예문",
                "4개의 선택지 발음(히라가나) 목록",
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
                당신은 일본어 교육 및 평가 콘텐츠 제작 전문가입니다. 특히, 학습자의 수준에 맞는 어휘와 문법적으로 정확한 예문을 구성하는 데 능숙합니다.
        
                [지시사항]
                아래 [한자 리스트]를 사용하여, 다음 과정을 통해 객관식 퀴즈를 생성해야 합니다. 리스트에 있는 각 한자당 하나의 문제를 순서대로 만들어야 합니다.
        
                각 한자에 대한 문제 생성은 아래 2단계로 이루어집니다.
        
                1단계: 단어 선정
                주어진 한자를 포함하는 일본어 단어를 하나 선정합니다. 이때, 단어의 난이도는 주어진 한자의 일반적인 사용 수준(예: JLPT 급수)과 비슷해야 합니다.
        
                2단계: 문제 생성
                1단계에서 선정한 단어를 사용하여, 이어지는 **[규칙]**과 **[JSON 스키마]**에 따라 문장형 객관식 문제를 생성합니다.
        
                [한자 리스트]
                %s
        
                [규칙]
        
                1. target_word와 예문의 형태 일치: target_word 필드에는 반드시 사전형이 아닌, sentence에 실제 사용된 활용형 단어를 그대로 기입해야 합니다. (예: 예문이 友達に会います이면, target_word는 会う가 아닌 会います가 되어야 합니다.)
        
                2. 예문 내의 target_word는 괄호로 감싸야 합니다.
        
                3. 선택지와 정답 기준: 선택지(options)와 정답(answer)은 위 1번 규칙에 따라 target_word로 지정된 활용형 단어의 실제 발음을 기준으로 만들어야 합니다. (예: target_word가 会います이면 정답은 あいます입니다.)
        
                4. 난이도 일치: 생성되는 **예문(sentence)**의 전체적인 어휘 수준과 문법 구조는, 문제의 기반이 되는 [한자]의 난이도와 비슷해야 합니다. 예를 들어, 쉬운 한자(N5 수준)에는 간단한 문장을, 어려운 한자(N1 수준)에는 좀 더 복잡한 문장을 사용해야 합니다.
        
                5. 최종 출력되는 문제의 순서는 [한자 리스트]의 순서와 정확히 일치해야 합니다.
        
                6. 각 문제 객체 안에는 kanji 프로퍼티를 포함하여, 어떤 한자로 문제를 만들었는지 명시해야 합니다.
        
                7. 오답 선택지 3개는 매우 중요합니다. 정답과 비슷하게 보이거나, 한자를 잘못 읽기 쉬운 발음, 혹은 흔한 실수(장음/단음, 탁음/반탁음 등)를 유도하는 그럴듯한 오답으로 구성해야 합니다.
        
                8. 모든 텍스트는 일본어로 작성하며, JSON 형식 규칙을 엄격히 준수합니다.
        
                [예시]
                {
                  "targetKanji": "会",
                  "targetWord": "会います",
                  "sentence": "友達に駅で(会います)。",
                  "options": [
                    "あいます",
                    "かいます",
                    "えいます",
                    "あう"
                  ],
                  "answer": "あいます"
                }
                """,
                kanjiList
        );
    }
}
