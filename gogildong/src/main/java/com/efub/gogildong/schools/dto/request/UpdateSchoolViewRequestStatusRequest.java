package com.efub.gogildong.schools.dto.request;

import com.efub.gogildong.schools.domain.RequestStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateSchoolViewRequestStatusRequest {

    private RequestStatus status;

    public UpdateSchoolViewRequestStatusRequest toEntity() {
        return UpdateSchoolViewRequestStatusRequest.builder()
                .status(this.status)
                .build();
    }
}
