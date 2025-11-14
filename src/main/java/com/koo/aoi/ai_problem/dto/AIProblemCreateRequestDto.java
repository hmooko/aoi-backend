package com.koo.aoi.ai_problem.dto;

import com.koo.aoi.ai_problem.domain.AIProblem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Schema(description = "AI 문제 생성 요청 DTO")
public class AIProblemCreateRequestDto {
    @Schema(description = "한자 리스트로 문제 수보다 리스트 길이가 길어야 함", required = true, example = "[\"家\", \"天\", \"火\", \"人\", \"月\"]")
    private final List<String> targets;
    @Schema(description = "문제 유형", required = true, example = "FIND_READING")
    private final AIProblem.ProblemType problemType;
    @Schema(description = "문제 수", required = true, example = "3")
    private final int Count;
}
