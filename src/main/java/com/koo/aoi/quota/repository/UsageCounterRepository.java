package com.koo.aoi.quota.repository;

import com.koo.aoi.quota.domain.UsageCounter;
import com.koo.aoi.quota.domain.UsageCounterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageCounterRepository extends JpaRepository<UsageCounter, UsageCounterId> {

}
