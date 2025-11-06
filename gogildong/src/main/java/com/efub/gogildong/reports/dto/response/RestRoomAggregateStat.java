package com.efub.gogildong.reports.dto.response;

import com.efub.gogildong.facility.domain.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RestRoomAggregateStat {
    private GenderType majorityGender;
    private Float avgDoorWidth;
    private Float avgDoorHeight;
    private Float avgToiletHeight;
    private Float avgGrabBar; // true 비율
    private Float avgIsAccessible; // true 비율
    private Float minDoorWidth;
    private Float maxDoorWidth;
}
