package com.koo.aoi.subscription.repository;

import com.koo.aoi.subscription.domain.UsageCounter;
import com.koo.aoi.subscription.domain.UsageCounterId;
import com.koo.aoi.user.domain.AoiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageCounterRepository extends JpaRepository<UsageCounter, UsageCounterId> {
    List<UsageCounter> findByUser(AoiUser user);
}
