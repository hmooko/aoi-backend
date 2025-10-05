package com.koo.aoi.subscription.service;

import com.koo.aoi.subscription.domain.*;
import com.koo.aoi.subscription.repository.FeatureRepository;
import com.koo.aoi.subscription.repository.PlanFeatureQuotaRepository;
import com.koo.aoi.subscription.repository.PlanRepository;
import com.koo.aoi.subscription.repository.UsageCounterRepository;
import com.koo.aoi.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsageCounterService {

    private final PlanFeatureQuotaService planFeatureQuotaService;
    private final PlanService planService;

    private final UsageCounterRepository usageCounterRepository;
    private final FeatureService featureService;

    public UsageCounter createUsageCounter(User user, Feature feature) {
        UsageCounter usageCounter = UsageCounter.builder()
                .id(new UsageCounterId(user.getId(), feature.getId()))
                .user(user)
                .feature(feature)
                .usageDate(LocalDate.now(ZoneId.of(user.getTimeZone().getTimeZoneId())))
                .usageCount(0)
                .build();

        return usageCounterRepository.save(usageCounter);
    }

    public UsageCounter getUsageCounter(User user, FeatureCode featureCode) {
        Feature feature = featureService.getFeature(featureCode);
        return usageCounterRepository.findById(new UsageCounterId(user.getId(), feature.getId()))
                .orElseThrow(() -> new NoSuchElementException("usageCounter를 찾을 수 없음"));
    }

    public List<UsageCounter> getUserCounter(User user) {
        return usageCounterRepository.findByUser(user);
    }

    public boolean isAiProblemUsageAvailable(User user) {
        resetDailyUsageCount(user);

        int quota = planFeatureQuotaService.getPlanFeatureQuota(user.getPlan(), featureService.getFeature(FeatureCode.AI_PROBLEM)).getQuota();
        int usageCount = getUsageCounter(user, FeatureCode.AI_PROBLEM).getUsageCount();

        return quota > usageCount;
    }

    public void resetDailyUsageCount(User user) {
        List<UsageCounter> usageCounterList = getUserCounter(user);

        for (UsageCounter counter : usageCounterList) {
            ZoneId userTimeZoneId = ZoneId.of(user.getTimeZone().getTimeZoneId());
            LocalDate now = ZonedDateTime.now(userTimeZoneId).toLocalDate();

            // 오늘 처음 사용
            if (counter.getUsageDate().isBefore(now)) {
                counter.setUsageCount(0);
            }
        }
    }
}
