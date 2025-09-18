package com.koo.aoi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.gemini.dto.GeminiResponse;
import com.koo.aoi.gemini.client.GeminiApiClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeminiApiClientTest {

    private MockWebServer mockWebServer;
    private GeminiApiClient geminiApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        String apiKey = "test-api-key";

        geminiApiClient = new GeminiApiClient(WebClient.builder(), apiKey, baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("Gemini API에 콘텐츠 생성을 성공적으로 요청하고 응답을 받는다")
    @Test
    void generateContent_Success() throws InterruptedException, JsonProcessingException {
        // given
        String mockResponseText = "This is a test response.";
        GeminiResponse mockResponse = new GeminiResponse(
                List.of(new GeminiResponse.Candidate(
                        new GeminiResponse.Content(
                                List.of(new GeminiResponse.Part(mockResponseText))
                        )
                ))
        );
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(objectMapper.writeValueAsString(mockResponse))
                        .addHeader("Content-Type", "application/json")
        );

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part("Test prompt"))
                )),
                null
        );

        // when
        GeminiResponse response = geminiApiClient.generateContent(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.candidates()).hasSize(1);
        assertThat(response.candidates().get(0).content().parts().get(0).text()).isEqualTo(mockResponseText);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("/v1beta/models/gemini-pro:generateContent");
        assertThat(recordedRequest.getHeader("x-goog-api-key")).isEqualTo("test-api-key");
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(objectMapper.writeValueAsString(request));
    }

    @DisplayName("Gemini API가 에러를 반환할 경우 WebClientResponseException을 발생시킨다")
    @Test
    void generateContent_ApiError() {
        // given
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal Server Error")
        );

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part("Test prompt"))
                )),
                null
        );

        // when/then
        assertThatThrownBy(() -> geminiApiClient.generateContent(request))
                .isInstanceOf(WebClientResponseException.InternalServerError.class);
    }
}
