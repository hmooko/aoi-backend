package com.koo.aoi.user.service;

import com.koo.aoi.auth.domain.OAuthAttributes;
import com.koo.aoi.subscription.service.FeatureService;
import com.koo.aoi.subscription.service.PlanService;
import com.koo.aoi.subscription.service.UsageCounterService;
import com.koo.aoi.subscription.domain.FeatureCode;
import com.koo.aoi.subscription.domain.PlanCode;
import com.koo.aoi.user.domain.AoiUser;
import com.koo.aoi.user.domain.Provider;
import com.koo.aoi.user.domain.Role;
import com.koo.aoi.user.domain.SupportedTimeZone;
import com.koo.aoi.user.repository.AoiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsageCounterService usageCounterService;
    private final FeatureService featureService;
    private final PlanService planService;

    private final AoiUserRepository aoiUserRepository;

    @Transactional
    public AoiUser register(OAuthAttributes attributes) {

        AoiUser savedUser = aoiUserRepository.findByEmailAndProvider(attributes.getEmail(), attributes.getProvider())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(AoiUser.builder()
                        .name(attributes.getName())
                        .email(attributes.getEmail())
                        .picture(attributes.getPicture())
                        .role(Role.USER)
                        .plan(planService.getPlan(PlanCode.FREE))
                        .timeZone(SupportedTimeZone.ASIA_SEOUL)
                        .provider(attributes.getProvider())
                        .build()
                );
        aoiUserRepository.save(savedUser);

        // UsageCounter - ai-problem 데이터 생성
        usageCounterService.createUsageCounter(
                savedUser,
                featureService.getFeature(FeatureCode.AI_PROBLEM)
        );

        return savedUser;
    }

    public AoiUser findByEmailAndProvider(String email, Provider provider) {
        return aoiUserRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email + " and provider: " + provider));
    }

    public AoiUser findById(Long id) {
        return aoiUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
}
