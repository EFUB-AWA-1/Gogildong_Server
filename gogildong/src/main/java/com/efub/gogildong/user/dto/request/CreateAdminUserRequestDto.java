package com.efub.gogildong.user.dto.request;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.dto.request.common.CreateUserRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAdminUserRequestDto extends CreateUserRequestDto {

}
