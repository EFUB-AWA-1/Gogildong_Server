package com.efub.gogildong.ai.dto.request;

import com.efub.gogildong.facility.domain.FacilityType;
import lombok.Data;

@Data
public class VerifyRequest {
    private String reportedFacilityType;
    private String reportedDoorType;
    // 뭐 더 추가하지 ...

    // MultipartFile image는 Controller에서 직접 수령
}
