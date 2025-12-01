package com.efub.gogildong.schools.dto.request;

import com.efub.gogildong.schools.domain.ReasonCategory;
import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.user.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SchoolViewRequestRequest {
    private Long schoolId;
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
    private ReasonCategory reasonCategory;
    private String requestReason;

    public SchoolViewRequest toEntity(School school, User user, RequestStatus status) {
        return SchoolViewRequest.builder()
                .status(status)
                .reasonCategory(this.getReasonCategory())
                .reason(this.requestReason)
                .phoneNumber(this.phoneNumber)
                .requestedAt(java.time.LocalDateTime.now())
                .user(user)
                .school(school)
                .build();
    }
}
