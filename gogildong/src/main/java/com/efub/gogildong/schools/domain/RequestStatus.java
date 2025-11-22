package com.efub.gogildong.schools.domain;

public enum RequestStatus {
    APPROVED,
    REJECTED,
    PENDING, // 열람 신청 등록 후 초기값. ai 검수 요청 후 대기 중. ai 검수 구현 후 성공 시 승인으로 바꾸어줘야함.
    BLOCKED
}
