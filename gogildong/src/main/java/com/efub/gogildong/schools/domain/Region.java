package com.efub.gogildong.schools.domain;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;

public enum Region {
    서울, 부산, 대구, 인천, 광주, 대전, 울산, 세종, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주;
    public static Region fromAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        String addr = address.trim();

        if (addr.startsWith("서울")) return 서울;
        if (addr.startsWith("부산")) return 부산;
        if (addr.startsWith("대구")) return 대구;
        if (addr.startsWith("인천")) return 인천;
        if (addr.startsWith("광주")) return 광주;
        if (addr.startsWith("대전")) return 대전;
        if (addr.startsWith("울산")) return 울산;
        if (addr.startsWith("세종")) return 세종;

        if (addr.startsWith("경기")) return 경기;
        if (addr.startsWith("강원")) return 강원;
        if (addr.startsWith("충청북도")) return 충북;
        if (addr.startsWith("충청남도")) return 충남;

        if (addr.startsWith("전북")) return 전북;
        if (addr.startsWith("전라남도")) return 전남;

        if (addr.startsWith("경상북도")) return 경북;
        if (addr.startsWith("경상남도")) return 경남;

        if (addr.startsWith("제주")) return 제주;

        throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
    }
}
