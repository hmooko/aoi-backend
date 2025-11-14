package com.koo.aoi.subscription.service;

import com.koo.aoi.subscription.domain.*;
import com.koo.aoi.subscription.repository.FeatureRepository;
import com.koo.aoi.subscription.repository.PlanFeatureQuotaRepository;
import com.koo.aoi.subscription.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanFeatureQuotaService {
    
    private final PlanFeatureQuotaRepository planFeatureQuotaRepository;
    private final PlanRepository planRepository;
    private final FeatureRepository featureRepository;
    
    public PlanFeatureQuota getPlanFeatureQuota(Plan plan, Feature feature) {
        return planFeatureQuotaRepository.findById(new PlanFeatureQuotaId(plan.getId(), feature.getId()))
                .orElseThrow(() -> new NoSuchElementException("PlanFeatureQuota를 찾을 수 없음"));
    }
}
