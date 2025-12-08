package com.efub.gogildong.schools.service;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.dto.request.CreateBuildingRequest;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBuildingsService {
    private final EntityFinder finder;

    /*
     * 학교 관리자가 해당 학교에 건물 추가
     * */
    @Transactional
    public void createBuilding(String loginId, CreateBuildingRequest request) {
        User user = finder.getUserByLoginId(loginId);
        Building createdBuilding = request.createBuilding();
        School school = user.getSchool();
        school.addBuilding(createdBuilding);
    }


}
