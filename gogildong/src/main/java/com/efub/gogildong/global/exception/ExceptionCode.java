package com.efub.gogildong.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    // 전체
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ClientExceptionCode.INTERNAL_SERVER_ERROR, "예상치 못한 서버에러가 발생했습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, ClientExceptionCode.ILLEGAL_ARGUMENT, "필수 파라미터 누락"),

    // 학교
    SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.SCHOOL_NOT_FOUND, "존재하지 않는 학교입니다."),
    SCHOOL_ALREADY_BOOKMARKED(HttpStatus.CONFLICT, ClientExceptionCode.SCHOOL_ALREADY_BOOKMARKED, "이미 즐겨찾기 한 학교입니다."),
    SCHOOL_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.SCHOOL_BOOKMARK_NOT_FOUND, "즐겨찾기하지 않은 학교입니다."),
    EDULEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.EDULEVEL_NOT_FOUND, "데이터 중 찾을 수 없는 학교급이 있습니다."),

    // 층
    FLOOR_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.FLOOR_NOT_FOUND, "존재하지 않는 층입니다."),
    FLOOR_NOT_FOUND_IN_SCHOOL(HttpStatus.NOT_FOUND, ClientExceptionCode.FLOOR_NOT_FOUND_IN_SCHOOL, "해당 학교에는 해당 층이 존재하지 않습니다."),

    // 시설
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.FACILITY_NOT_FOUND, "존재하지 않는 시설입니다."),
    FACILITY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, ClientExceptionCode.FACILITY_TYPE_NOT_FOUND, "존재하지 않는 시설 종류입니다."),
    FACILITY_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST, ClientExceptionCode.FACILITY_TYPE_BAD_REQUEST, "유효하지 않은 시설 타입입니다."),

    // 제보
    DOOR_TYPE_BAD_REQUEST(HttpStatus.BAD_REQUEST, ClientExceptionCode.DOOR_TYPE_BAD_REQUEST, "유효하지 않은 문 타입입니다."),

    // 허가
    UNAUTHORIZED_SCHOOL_ACCESS(HttpStatus.UNAUTHORIZED, ClientExceptionCode.UNAUTHORIZED_SCHOOL_ACCESS, "허가되지 않은 사용자가 학교 정보에 접근했습니다.");

    private final HttpStatus httpStatus;
    private final ClientExceptionCode clientExceptionCode;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, ClientExceptionCode clientExceptionCode, String message) {
        this.httpStatus = httpStatus;
        this.clientExceptionCode = clientExceptionCode;
        this.message = message;
    }
}
