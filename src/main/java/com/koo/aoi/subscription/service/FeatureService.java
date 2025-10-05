package com.koo.aoi.subscription.service;

import com.koo.aoi.subscription.domain.Feature;
import com.koo.aoi.subscription.domain.FeatureCode;
import com.koo.aoi.subscription.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    public Feature getFeature(FeatureCode featureCode) {
        return featureRepository.findByCode(featureCode)
                .orElseThrow(() -> new NoSuchElementException("feature 코드에 맞는 feature가 존재하지 않음"));
    }
}
