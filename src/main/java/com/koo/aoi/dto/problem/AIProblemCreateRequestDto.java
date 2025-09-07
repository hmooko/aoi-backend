package com.koo.aoi.dto.problem;

import com.koo.aoi.domain.AIProblem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AIProblemCreateRequestDto {
    private final List<String> targets;
    private final AIProblem.ProblemType problemType;
    private final int Count;
}
