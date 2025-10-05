package com.koo.aoi.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpRequest {

    @Email
    @NotBlank
    @Schema(description = "email 형식", example = "example@example.com")
    private String uid;

    @NotBlank
    @Schema(description = "닉네임", example = "토끼1")
    private String nickname;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Schema(description = "비밀번호", example = "qwer1234")
    private String password;
}
