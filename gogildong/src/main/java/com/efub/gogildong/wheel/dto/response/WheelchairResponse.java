package com.efub.gogildong.wheel.dto.response;

import com.efub.gogildong.wheel.domain.Wheelchair;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WheelchairResponse {

    private Long wheelChairId;
    private String wheelChairName;
    private int width;
    private int maxThreshold;
    private boolean mainUsed;

    public static WheelchairResponse of(Wheelchair wheelchair, boolean mainUsed) {
        return new WheelchairResponse(
                wheelchair.getId(),
                wheelchair.getWheelchairName(),
                wheelchair.getWidth(),
                wheelchair.getMaxThreshold(),
                mainUsed
        );
    }
}
