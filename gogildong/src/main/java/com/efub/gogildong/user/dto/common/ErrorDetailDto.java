package com.efub.gogildong.user.dto.common;

public record ErrorDetailDto(
        String field,
        String message,
        String code
) {}