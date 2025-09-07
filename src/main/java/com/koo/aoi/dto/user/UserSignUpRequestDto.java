package com.koo.aoi.dto.user;

import com.koo.aoi.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    private String username;
    private String email;
    private String password;
}
