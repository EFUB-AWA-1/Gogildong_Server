package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchoolViewRequestResponse {
    private Long requestId;
    private Long schoolId;
    private String schoolName;
    private Long userId;
    private RequestStatus status;
    private String message;
    private LocalDateTime createdAt;

    public static SchoolViewRequestResponse from(SchoolViewRequest schoolViewRequest) {
        return SchoolViewRequestResponse.builder()
                .requestId(schoolViewRequest.getRequestId())
                .schoolId(schoolViewRequest.getSchool().getSchoolId())
                .schoolName(schoolViewRequest.getSchool().getSchoolName())
                .userId(schoolViewRequest.getUser().getUserId())
                .status(schoolViewRequest.getStatus())
                .message(generateMessage(schoolViewRequest.getStatus()))
                .createdAt(schoolViewRequest.getRequestedAt())
                .build();
    }

    private static String generateMessage(RequestStatus status) {
        switch (status) {
            case PENDING:
                return "열람신청이 성공적으로 접수되었습니다.";
            case APPROVED:
                return "열람신청이 성공적으로 승인되었습니다.";
            case REJECTED:
                return "부적절한 신청 사유로 거부되었습니다.";
            default:
                return "알 수 없는 상태입니다.";
        }
    }
}
