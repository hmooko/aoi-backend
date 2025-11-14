package com.koo.aoi.subscription.repository;

import com.koo.aoi.subscription.domain.Plan;
import com.koo.aoi.subscription.domain.PlanCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByCode(PlanCode code);
}
