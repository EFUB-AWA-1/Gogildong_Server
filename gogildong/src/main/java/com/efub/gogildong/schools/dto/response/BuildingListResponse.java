package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BuildingListResponse {
    List<BuildingSummaryResponse> buildings;
}
