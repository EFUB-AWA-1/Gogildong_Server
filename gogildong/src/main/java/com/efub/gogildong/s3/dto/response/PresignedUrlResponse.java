package com.efub.gogildong.s3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {

    private String uploadUrl;
    private String objectKey;
    private String fileUrl; // 최종 접근 가능한 url
}
