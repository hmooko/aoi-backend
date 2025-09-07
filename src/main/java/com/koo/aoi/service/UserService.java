package com.koo.aoi.service;

import com.koo.aoi.domain.User;
import com.koo.aoi.dto.user.UserResponseDto;
import com.koo.aoi.dto.user.UserSignUpRequestDto;
import com.koo.aoi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto request) {
        validateDuplicateUsername(request.getUsername());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(User.Role.USER)
                .build();

        User saved = userRepository.save(user);
        return new UserResponseDto(saved);
    }

    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + id));
        return new UserResponseDto(user);
    }

    public UserResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: username=" + username));
        return new UserResponseDto(user);
    }

    public List<UserResponseDto> listAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    private void validateDuplicateUsername(String username) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
    }
}

