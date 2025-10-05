package com.koo.aoi.subscription.repository;

import com.koo.aoi.subscription.domain.PlanFeatureQuota;
import com.koo.aoi.subscription.domain.PlanFeatureQuotaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanFeatureQuotaRepository extends JpaRepository<PlanFeatureQuota, PlanFeatureQuotaId> {

}
