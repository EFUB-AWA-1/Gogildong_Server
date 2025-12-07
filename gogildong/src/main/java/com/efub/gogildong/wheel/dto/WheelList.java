package com.efub.gogildong.wheel.dto;

import com.efub.gogildong.wheel.dto.response.WheelchairResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WheelList {

    private List<WheelchairResponse> wheelchairs;
}
