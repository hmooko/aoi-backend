package com.koo.aoi.subscription.repository;

import com.koo.aoi.subscription.domain.Feature;
import com.koo.aoi.subscription.domain.FeatureCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByCode(FeatureCode code);
}
