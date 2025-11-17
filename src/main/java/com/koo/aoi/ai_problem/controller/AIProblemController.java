package com.koo.aoi.ai_problem.controller;

import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import com.koo.aoi.ai_problem.dto.AIProblemResponseDto;
import com.koo.aoi.ai_problem.service.AIProblemService;
import com.koo.aoi.global.apiPayload.AoiApiResponse;
import com.koo.aoi.global.apiPayload.code.GeneralSuccessCode;
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

@RestController
@RequiredArgsConstructor
public class AIProblemController {

    private final AIProblemService aiProblemService;

    @SecurityRequirement(name = "JWT")
    @PostMapping("/api/v1/ai-problem")
    public AoiApiResponse<List<AIProblemResponseDto>> createAIProblems(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AIProblemCreateRequestDto requestDto) {

        String userId = userDetails.getUsername();

        List<AIProblemResponseDto> problems = aiProblemService.createAIProblems(Long.parseLong(userId), requestDto);

        return AoiApiResponse.onSuccess(
                GeneralSuccessCode.GET_SUCCESS,
                problems
        );
    }
}
