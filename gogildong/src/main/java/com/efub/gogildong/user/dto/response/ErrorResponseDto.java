package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.user.dto.common.ErrorDetailDto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp,

        List<ErrorDetailDto> errors
) {
    public ErrorResponseDto(int status, String error, String message, String path, LocalDateTime timestamp) {
        this(status, error, message, path, timestamp, List.of());
    }
}