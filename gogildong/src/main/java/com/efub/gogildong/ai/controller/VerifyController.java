package com.efub.gogildong.ai.controller;

import com.efub.gogildong.ai.dto.request.VerifyRequest;
import com.efub.gogildong.ai.dto.response.VerifyResultResponse;
import com.efub.gogildong.ai.service.VerifyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerifyController {

    private final VerifyService verifyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VerifyResultResponse> verify(
            @RequestPart("metadata") String metadataJson,
            @RequestPart("image") String imageUrl) throws Exception {

        System.out.println("== CONTROLLER 들어옴 ==");

        ObjectMapper om = new ObjectMapper();
        VerifyRequest metadata = om.readValue(metadataJson, VerifyRequest.class);

        VerifyResultResponse result = verifyService.verifyImageWithOpenAI(imageUrl, metadata);
        return ResponseEntity.ok(result);
    }
}
