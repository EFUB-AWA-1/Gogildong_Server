package com.efub.gogildong.global.handler;

import com.efub.gogildong.global.exception.BusinessValidationException;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.exception.dto.ExceptionResponse;
import com.efub.gogildong.user.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(GoGildongException.class)
    public ResponseEntity<ExceptionResponse> handleGoGildongException(GoGildongException e, HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                e.getHttpStatusCode().value(),
                e.getExceptionCodeName(),
                e.getMessage(),
                request.getRequestURI(), ZonedDateTime.now()
        );
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = "유효하지 않은 입력입니다.";
        FieldError fieldError = e.getFieldError();
        if(fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();
        }
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ExceptionCode.ILLEGAL_ARGUMENT.name(),
                errorMessage,
                request.getRequestURI(), ZonedDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException runtimeException,
                                                                    HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                500,
                ExceptionCode.INTERNAL_SERVER_ERROR.getClientExceptionCode().name(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(),
                request.getRequestURI(), ZonedDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // 비즈니스 로직 다중 오류 처리 (회원가입 전용)
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessValidationException(BusinessValidationException e, HttpServletRequest request) {

        ErrorResponseDto response = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "BUSINESS_VALIDATION_FAILED",
                "입력값 검증에 실패했습니다. 상세 오류를 확인하세요.",
                request.getRequestURI(),
                LocalDateTime.now(),
                e.getErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}