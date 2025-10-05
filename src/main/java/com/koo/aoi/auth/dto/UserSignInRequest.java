package com.koo.aoi.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignInRequest {

    @Email
    @NotBlank
    @Schema(description = "이메일 형식 uid", example = "example@example.com")
    private String uid;

    @NotBlank
    @Schema(description = "비밀번호", example = "qwer1234")
    private String password;
}
