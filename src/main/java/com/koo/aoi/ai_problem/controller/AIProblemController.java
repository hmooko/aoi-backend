package com.koo.aoi.ai_problem.controller;

import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;
import com.koo.aoi.ai_problem.dto.AIProblemResponseDto;
import com.koo.aoi.ai_problem.service.AIProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AIProblemController {

    private final AIProblemService aiProblemService;

    @PostMapping("/api/v1/create-ai-problems")
    public ResponseEntity<List<AIProblemResponseDto>> createAIProblems(@RequestBody AIProblemCreateRequestDto requestDto) {
        List<AIProblemResponseDto> problems = aiProblemService.createAIProblems(requestDto);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

//    @GetMapping("/api/v1/projects")
//    public ResponseEntity<List<AIProject>> getAIProjects() {
//
//    }
}
