package com.koo.aoi.ai_problem.service;

import com.koo.aoi.gemini.client.GeminiApiClient;
import com.koo.aoi.ai_problem.domain.AIProblem;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import com.koo.aoi.ai_problem.dto.AIProblemResponseDto;
import com.koo.aoi.ai_problem.repository.AIProblemRepository;
import com.koo.aoi.ai_problem.service.strategy.FillReadingProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.FindKanjiProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.FindReadingProblemStrategy;
import com.koo.aoi.ai_problem.service.strategy.ProblemCreationStrategy;
import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.gemini.dto.GeminiResponse;
import org.springframework.stereotype.Service;
 

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAlias;

@Service
public class AIProblemService {
    private final GeminiApiClient geminiApiClient;
    // EnumMap is a highly efficient Map implementation for use with enum keys.
    private final Map<ProblemType, ProblemCreationStrategy> strategyMap;

    // Spring will automatically inject all beans that implement ProblemCreationStrategy into the list.
    public AIProblemService(GeminiApiClient geminiApiClient, AIProblemRepository aiProblemRepository) {
        this.geminiApiClient = geminiApiClient;
        this.strategyMap = new EnumMap<>(ProblemType.class);

        List<ProblemCreationStrategy> strategies = List.of(
                new FindReadingProblemStrategy(),
                new FindKanjiProblemStrategy(),
                new FillReadingProblemStrategy()
        );
        for (ProblemCreationStrategy strategy : strategies) {
            strategyMap.put(strategy.getProblemType(), strategy);
        }
    }

    public List<AIProblemResponseDto> createAIProblems(AIProblemCreateRequestDto requestDto) {

        ProblemType type = requestDto.getProblemType();

        ProblemCreationStrategy strategy = strategyMap.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported problem type: " + type);
        }

        GeminiRequest geminiRequest = strategy.createRequest(requestDto);

        GeminiResponse response = geminiApiClient.generateContent(geminiRequest);

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new IllegalStateException("Empty response from Gemini API");
        }

        // Extract JSON text from the first candidate/part
        GeminiResponse.Candidate candidate = response.candidates().get(0);
        if (candidate == null || candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
            throw new IllegalStateException("Gemini response has no content parts");
        }
        String rawText = candidate.content().parts().get(0).text();
        String json = sanitizeJson(rawText);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ProblemPayload> payloads = mapper.readValue(json, new TypeReference<List<ProblemPayload>>() {});

            LocalDateTime now = LocalDateTime.now();
            List<AIProblem> problems = payloads.stream().map(p -> {
                AIProblem problem = new AIProblem();
                problem.setProblemType(type);
                problem.setSentence(p.sentence);
                problem.setTargetKanji(p.targetKanji);
                problem.setOptions(p.options);
                problem.setAnswer(p.answer);
                problem.setTargetWord(p.targetWord);
                problem.setCreateAt(now);
                problem.setUpdateAt(now);
                return problem;
            }).toList();

            return problems.stream()
                    .map(AIProblemResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Gemini response", e);
        }
    }

    private String sanitizeJson(String text) {
        if (text == null) return "[]";
        String trimmed = text.trim();
        // Remove markdown code fences if present
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            trimmed = trimmed.substring(3, trimmed.length() - 3).trim();
            // If the fence includes a language hint like ```json, remove the first line
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0 && trimmed.substring(0, firstNewline).matches("[a-zA-Z0-9]+")) {
                trimmed = trimmed.substring(firstNewline + 1).trim();
            }
        }
        return trimmed;
    }

    // Minimal DTO matching expected Gemini JSON schema
    private static class ProblemPayload {
        public String targetKanji;
        public String targetWord;
        @JsonAlias({"sentence", "question"})
        public String sentence;
        public List<String> options;
        public String answer;
    }
}
