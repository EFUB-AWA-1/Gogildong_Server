package com.efub.gogildong.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateInternalSchoolRequestDto {

    @NotBlank
    private String schoolCode;

}
