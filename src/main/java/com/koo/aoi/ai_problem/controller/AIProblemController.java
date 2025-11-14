package com.koo.aoi.ai_problem.controller;

import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import com.koo.aoi.ai_problem.dto.AIProblemResponseDto;
import com.koo.aoi.ai_problem.service.AIProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AI Problem", description = "AI 문제 생성 API")
@RestController
@RequiredArgsConstructor
public class AIProblemController {

    private final AIProblemService aiProblemService;

    @Operation(summary = "AI 문제 생성", description = "AI를 이용하여 문제를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문제 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/api/v1/ai-problem")
    public ResponseEntity<List<AIProblemResponseDto>> createAIProblems(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "문제 생성 요청 DTO", required = true) @RequestBody AIProblemCreateRequestDto requestDto) {

        String userId = userDetails.getUsername();

        List<AIProblemResponseDto> problems = aiProblemService.createAIProblems(Long.parseLong(userId), requestDto);
        return ResponseEntity.ok(problems);
    }
}
