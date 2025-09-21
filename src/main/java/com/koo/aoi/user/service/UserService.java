package com.koo.aoi.user.service;

import com.koo.aoi.user.domain.User;
import com.koo.aoi.user.dto.UserSignInRequest;
import com.koo.aoi.user.dto.UserSignUpRequest;
import com.koo.aoi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(UserSignUpRequest request) {
        userRepository.findByUid(request.getUid()).ifPresent(user -> {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        });

        User user = User.builder()
                .uid(request.getUid())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(User.Role.USER)
                .timeZone(User.TimeZone.KOREA)
                .build();

        return userRepository.save(user);
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
}
