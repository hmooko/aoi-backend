package com.koo.aoi.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    private String username;
    private String email;
    private String password;
}
