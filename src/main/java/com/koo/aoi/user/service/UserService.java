package com.koo.aoi.user.service;

import com.koo.aoi.auth.AuthenticatedUser;
import com.koo.aoi.subscription.service.FeatureService;
import com.koo.aoi.subscription.service.UsageCounterService;
import com.koo.aoi.subscription.domain.FeatureCode;
import com.koo.aoi.subscription.domain.PlanCode;
import com.koo.aoi.subscription.repository.FeatureRepository;
import com.koo.aoi.subscription.repository.PlanRepository;
import com.koo.aoi.user.domain.User;
import com.koo.aoi.auth.dto.UserSignInRequest;
import com.koo.aoi.auth.dto.UserSignUpRequest;
import com.koo.aoi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsageCounterService usageCounterService;
    private final FeatureService featureService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final FeatureRepository featureRepository;

    @Transactional
    public User register(UserSignUpRequest request) {
        userRepository.findByUid(request.getUid()).ifPresent(user -> {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        });

        // user 데이터 생성
        User user = User.builder()
                .uid(request.getUid())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(User.Role.USER)
                .plan(planRepository.findByCode(PlanCode.FREE).orElseThrow(() -> new NoSuchElementException("Plan 코드에 맞는 Plan이 존재하지 않음")))
                .timeZone(User.TimeZone.ASIA_SEOUL)
                .build();

        // user save
        User savedUser = userRepository.save(user);

        // UsageCounter - ai-problem 데이터 생성
        usageCounterService.createUsageCounter(
                savedUser,
                featureService.getFeature(FeatureCode.AI_PROBLEM)
        );

        return savedUser;
    }

    @Transactional(readOnly = true)
    public User authenticate(UserSignInRequest request) {
        User user = userRepository.findByUid(request.getUid())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new IllegalStateException("인증 정보가 존재하지 않습니다.");
        }

        return userRepository.findByUid(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }
}
