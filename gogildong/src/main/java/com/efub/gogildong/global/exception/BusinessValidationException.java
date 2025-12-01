package com.efub.gogildong.global.exception;

import com.efub.gogildong.user.dto.common.ErrorDetailDto;

import java.util.List;

public class BusinessValidationException extends RuntimeException{

    private final List<ErrorDetailDto> errors;

    public BusinessValidationException(List<ErrorDetailDto> errors) {
        super();
        this.errors = errors;
    }

    public List<ErrorDetailDto> getErrors() {
        return errors;
    }
}