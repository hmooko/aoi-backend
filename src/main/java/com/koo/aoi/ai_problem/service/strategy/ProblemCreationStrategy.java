package com.koo.aoi.ai_problem.service.strategy;

import com.koo.aoi.ai_problem.domain.AIProblem.ProblemType;
import com.koo.aoi.gemini.dto.GeminiRequest;
import com.koo.aoi.ai_problem.dto.AIProblemCreateRequestDto;

public interface ProblemCreationStrategy {

    // 이 전략이 어떤 문제 유형을 처리하는지 반환
    ProblemType getProblemType();

    // 문제 유형에 맞는 GeminiRequest를 생성
    GeminiRequest createRequest(AIProblemCreateRequestDto requestDto);


}
