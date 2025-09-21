package com.koo.aoi.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignInRequest {

    @Email
    @NotBlank
    private String uid;

    @NotBlank
    private String password;
}
