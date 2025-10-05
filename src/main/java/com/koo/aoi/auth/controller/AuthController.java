package com.koo.aoi.auth.controller;

import com.koo.aoi.auth.jwt.JwtTokenProvider;
import com.koo.aoi.user.domain.User;
import com.koo.aoi.auth.dto.AuthTokenResponse;
import com.koo.aoi.auth.dto.UserSignInRequest;
import com.koo.aoi.auth.dto.UserSignUpRequest;
import com.koo.aoi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("auth/users/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserSignUpRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/users/signin")
    public ResponseEntity<AuthTokenResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        User user = userService.authenticate(request);
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new AuthTokenResponse(token));
    }
}
