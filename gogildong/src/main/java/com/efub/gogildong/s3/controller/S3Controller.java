package com.efub.gogildong.s3.controller;

import com.efub.gogildong.s3.dto.response.PresignedUrlResponse;
import com.efub.gogildong.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public PresignedUrlResponse getPresignedUrl(
            @RequestParam("filename") String filename,
            @RequestParam("contentType") String contentType
    ) {
        return s3Service.createUploadUrl(filename, contentType);
    }
}
