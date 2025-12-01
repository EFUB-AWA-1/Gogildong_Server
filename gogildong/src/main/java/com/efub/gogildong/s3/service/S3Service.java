package com.efub.gogildong.s3.service;

import com.efub.gogildong.s3.dto.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Value("${app.s3.region}")
    private String region;

    public PresignedUrlResponse createUploadUrl(String filename, String contentType) {

        String objectKey = "uploads/" + UUID.randomUUID()+"_"+filename;
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        // url 유효시간
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String uploadUrl = presignedRequest.url().toString();

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objectKey);

        return new PresignedUrlResponse(uploadUrl, objectKey, fileUrl);


    }
}
