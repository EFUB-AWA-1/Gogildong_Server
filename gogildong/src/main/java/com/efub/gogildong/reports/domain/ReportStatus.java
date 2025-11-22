package com.efub.gogildong.reports.domain;

public enum ReportStatus {
    APPROVED,
    REJECTED,
    PENDING // 제보 등록 후 초기값. ai 검수 요청 후 대기 중. ai 검수 구현 후 성공 시 승인으로 바꾸어줘야함.
}
