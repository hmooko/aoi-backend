package com.koo.aoi.ai_problem.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koo.aoi.ai_problem.domain.AIProblem;
import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import com.koo.aoi.ai_problem.dto.AIProblemResponseDto;
import com.koo.aoi.ai_problem.service.strategy.FillReadingProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.FindKanjiProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.FindReadingProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.ProblemCreationStrategy;
import com.koo.aoi.gemini.client.GeminiApiClient;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.gemini.dto.GeminiResponse;
import com.koo.aoi.subscription.service.UsageCounterService;
import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIProblemService {

    private final GeminiApiClient geminiApiClient;
    private final UsageCounterService usageCounterService;
    private final UserService userService;
    private final Map<ProblemType, ProblemCreationStrategy> strategyMap;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper is thread-safe

    public AIProblemService(
            GeminiApiClient geminiApiClient,
            UsageCounterService usageCounterService,
            UserService userService
    ) {
        this.geminiApiClient = geminiApiClient;
        this.usageCounterService = usageCounterService;
        this.userService = userService;
        this.strategyMap = initializeStrategies();
    }

    private Map<ProblemType, ProblemCreationStrategy> initializeStrategies() {
        Map<ProblemType, ProblemCreationStrategy> strategies = new EnumMap<>(ProblemType.class);
        List.of(
                new FindReadingProblemStrategy(),
                new FindKanjiProblemStrategy(),
                new FillReadingProblemStrategy()
        ).forEach(strategy -> strategies.put(strategy.getProblemType(), strategy));
        return strategies;
    }

    /**
     * AI 기반 문제를 생성합니다.
     *
     * @param userId     요청한 사용자의 ID
     * @param requestDto 문제 생성 요청 DTO
     * @return 생성된 AI 문제 목록
     */
    public List<AIProblemResponseDto> createAIProblems(Long userId, AIProblemCreateRequestDto requestDto) {
        // 1. 사용자 사용량 확인
        checkUsageQuota(userId);

        // 2. 문제 유형에 맞는 전략 선택
        ProblemType type = requestDto.getProblemType();
        ProblemCreationStrategy strategy = selectStrategy(type);

        // 3. Gemini API를 통해 문제 내용 생성
        String jsonResponse = fetchProblemsFromGemini(strategy, requestDto);

        // 4. API 응답을 파싱하여 AIProblem 객체 목록으로 변환
        List<AIProblem> problems = parseAndMapProblems(jsonResponse, type);

        // 5. AIProblem 목록을 DTO로 변환하여 반환
        return convertToDto(problems);
    }

    /**
     * 사용자의 AI 문제 생성 가능 여부를 확인합니다.
     *
     * @param userId 확인할 사용자의 ID
     * @throws IllegalStateException 사용량 한도 초과 시
     */
    private void checkUsageQuota(Long userId) {
        AoiUser currentUser = userService.findById(userId);
        if (!usageCounterService.isAiProblemUsageAvailable(currentUser)) {
            // TODO: 사용량 한도 초과 에러 처리 클래스 만들어야 함
            throw new IllegalStateException("사용량 한도 초과");
        }
    }

    /**
     * 요청된 문제 유형에 맞는 생성 전략을 반환합니다.
     *
     * @param type 문제 유형
     * @return 해당 유형의 ProblemCreationStrategy
     * @throws IllegalArgumentException 지원하지 않는 문제 유형일 경우
     */
    private ProblemCreationStrategy selectStrategy(ProblemType type) {
        ProblemCreationStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported problem type: " + type);
        }
        return strategy;
    }

    /**
     * Gemini API를 호출하여 문제 생성을 요청하고, 응답을 JSON 문자열로 반환합니다.
     *
     * @param strategy   사용할 문제 생성 전략
     * @param requestDto 문제 생성 요청 DTO
     * @return Gemini API로부터 받은 JSON 응답 문자열
     * @throws IllegalStateException API 응답이 비어있거나 유효하지 않을 경우
     */
    private String fetchProblemsFromGemini(ProblemCreationStrategy strategy, AIProblemCreateRequestDto requestDto) {
        GeminiRequest geminiRequest = strategy.createRequest(requestDto);
        GeminiResponse response = geminiApiClient.generateContent(geminiRequest);

        System.out.println(response); // 로거 사용을 권장합니다 (e.g., log.debug("Gemini Response: {}", response);)

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new IllegalStateException("Empty response from Gemini API");
        }

        GeminiResponse.Candidate candidate = response.candidates().get(0);
        if (candidate == null || candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
            throw new IllegalStateException("Gemini response has no content parts");
        }

        String rawText = candidate.content().parts().get(0).text();
        return sanitizeJson(rawText);
    }

    /**
     * JSON 문자열을 파싱하여 AIProblem 엔티티 목록으로 변환합니다.
     *
     * @param json API로부터 받은 JSON 문자열
     * @param type 문제 유형
     * @return AIProblem 엔티티 목록
     * @throws IllegalStateException JSON 파싱 실패 시
     */
    private List<AIProblem> parseAndMapProblems(String json, ProblemType type) {
        try {
            List<ProblemPayload> payloads = objectMapper.readValue(json, new TypeReference<>() {});
            return payloads.stream()
                    .map(payload -> payload.toEntity(type))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Gemini response", e);
        }
    }

    /**
     * AIProblem 엔티티 목록을 AIProblemResponseDto 목록으로 변환합니다.
     *
     * @param problems AIProblem 엔티티 목록
     * @return AIProblemResponseDto 목록
     */
    private List<AIProblemResponseDto> convertToDto(List<AIProblem> problems) {
        return problems.stream()
                .map(AIProblemResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Gemini API 응답에서 마크다운 코드 블록 등을 제거하여 순수한 JSON 문자열만 추출합니다.
     *
     * @param text 원본 텍스트
     * @return 정제된 JSON 문자열
     */
    private String sanitizeJson(String text) {
        if (text == null) return "[]";
        String trimmed = text.trim();
        // 마크다운 코드 블록(```json ... ```) 제거
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            trimmed = trimmed.substring(3, trimmed.length() - 3).trim();
            // 언어 힌트가 포함된 첫 줄 제거 (e.g., ```json)
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0 && trimmed.substring(0, firstNewline).matches("[a-zA-Z0-9]+")) {
                trimmed = trimmed.substring(firstNewline + 1).trim();
            }
        }
        return trimmed;
    }

    /**
     * Gemini API의 JSON 응답 스키마에 매칭되는 내부 DTO 클래스입니다.
     */
    private static class ProblemPayload {
        public String targetKanji;
        public String targetWord;
        @JsonAlias({"sentence", "question"})
        public String sentence;
        public List<String> options;
        public String answer;

        /**
         * ProblemPayload DTO를 AIProblem 엔티티로 변환합니다.
         *
         * @param type 문제 유형
         * @return AIProblem 엔티티
         */
        public AIProblem toEntity(ProblemType type) {
            AIProblem problem = new AIProblem();
            problem.setProblemType(type);
            problem.setSentence(this.sentence);
            problem.setTargetKanji(this.targetKanji);
            problem.setOptions(this.options);
            problem.setAnswer(this.answer);
            problem.setTargetWord(this.targetWord);
            return problem;
        }
    }
}
