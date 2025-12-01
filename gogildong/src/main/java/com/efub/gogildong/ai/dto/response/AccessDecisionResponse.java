package com.efub.gogildong.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessDecisionResponse {
    private boolean approved;
    private String reason;
}
