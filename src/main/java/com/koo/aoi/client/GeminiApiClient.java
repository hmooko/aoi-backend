package com.koo.aoi.client;

import com.koo.aoi.dto.gemini.GeminiRequest;
import com.koo.aoi.dto.gemini.GeminiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Gemini API와의 통신을 담당하는 순수 클라이언트 클래스.
 * 이 클래스는 비즈니스 로직을 포함하지 않습니다.
 */
@Component
public class GeminiApiClient {

    private final WebClient webClient;
    private static final String GEMINI_MODEL = "gemini-2.5-flash";
    private static final Logger log = LoggerFactory.getLogger(GeminiApiClient.class);

    @Autowired
    public GeminiApiClient(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.key}") String apiKey
    ) {
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    /**
     * 테스트용 생성자. Base URL을 주입받을 수 있습니다.
     */
    GeminiApiClient(WebClient.Builder webClientBuilder, String apiKey, String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    /**
     * Gemini API에 비동기적으로 요청을 보내고 응답을 받는 범용 메소드.
     * 이 메소드는 프롬프트 생성을 포함하지 않고, 단지 통신만 담당합니다.
     * WebClient를 사용하여 non-blocking I/O를 처리하며, 에러 처리는 스트림을 구독하는 측에서 담당해야 합니다.
     *
     * @param request 서비스 계층에서 완전히 구성된 요청 객체
     * @return Gemini API의 응답을 담은 Mono 객체
     */
    public GeminiResponse generateContent(GeminiRequest request) {
        log.info("Sending request to Gemini API for model: {}", GEMINI_MODEL);
        return webClient.post()
                .uri("/v1beta/models/{model}:generateContent", GEMINI_MODEL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .doOnSuccess(response -> log.info("Successfully received response from Gemini API."))
                .doOnError(error -> log.error("Error occurred while calling Gemini API", error))
                .block();
    }
}
