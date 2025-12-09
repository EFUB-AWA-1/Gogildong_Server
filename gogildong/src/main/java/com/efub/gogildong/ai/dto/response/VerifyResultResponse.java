package com.efub.gogildong.ai.dto.response;

import lombok.Data;

@Data
public class VerifyResultResponse {
    private String predictedType;
    private boolean isMatched;          // 시설 타입 매칭
    private double confidence;
    private String predictedDoorType;
    private Boolean isDoorMatched;      // 도어 타입 매칭
    private String reason;
}
