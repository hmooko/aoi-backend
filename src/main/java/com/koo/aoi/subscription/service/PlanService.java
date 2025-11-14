package com.koo.aoi.subscription.service;

import com.koo.aoi.subscription.domain.Plan;
import com.koo.aoi.subscription.domain.PlanCode;
import com.koo.aoi.subscription.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan getPlan(PlanCode code) {
        return planRepository.findByCode(code).orElseThrow(() -> new NoSuchElementException("Plan 코드에 맞는 Plan이 존재하지 않음"));
    }
}
