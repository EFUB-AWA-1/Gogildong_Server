package com.efub.gogildong.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    private String username;
    private String email;
    private String phone;
}
