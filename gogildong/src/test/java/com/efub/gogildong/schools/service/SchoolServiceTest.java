package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.SchoolListResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolService schoolService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private Pageable pageable;

    private final List<School> mockSchools = new ArrayList<>();

    private final double lat = 37.56;
    private final double lng = 126.97;
    private final int radius = 1000;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        mockSchool = School.builder()
                .schoolCode("asdf4532")
                .schoolName("위시고등학교")
                .address("서울 성동구 왕십리로 83-21 에스엠엔터테인먼트")
                .schoolCode("wish4332")
                .location(geometryFactory.createPoint(new Coordinate(lng, lat)))
                .build();
        mockSchools.add(mockSchool);

        pageable = PageRequest.of(0, 20);
    }

    @Test
    @DisplayName("근처 학교 조회 시 학교 리스트 반환")
    void getNearbySchools_returnsSchoolList(){
        // given
        Page<School> page = new PageImpl<>(List.of(mockSchool), pageable, 1);
        when(schoolRepository.findSchoolsWithinRadiusAndTag(lat, lng, radius, null, pageable))
                .thenReturn(page);

        // when
        SchoolListResponse response = schoolService.getNearbySchools(lat, lng, TagCategory.all, radius,pageable);

        // then
        assertThat(response.getSchools()).hasSize(1);
        assertThat(response.getSchools().get(0).getSchoolId()).isEqualTo(mockSchool.getSchoolId());
    }

    @Test
    @DisplayName("근처 학교 조회 시 404 에러")
    void getNearbySchools_throwsException_whenEmpty(){
        // given
        when(schoolRepository.findSchoolsWithinRadiusAndTag(anyDouble(), anyDouble(), anyDouble(), any(), any()))
                .thenReturn(Page.empty());

        // when & then
        GoGildongException exception = assertThrows(
                GoGildongException.class,
                () -> schoolService.getNearbySchools(37.56, 126.97, TagCategory.restroom, 1.0, pageable)
        );
        // 예외 메시지 확인
        assertThat(exception.getMessage()).isEqualTo(ExceptionCode.SCHOOL_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("검색어 기반 학교 조회 시 학교 리스트 반환")
    void getSchoolsByQuery_returnsSchoolList(){
        // given
        Page<School> page = new PageImpl<>(List.of(mockSchool), pageable, 1);

        when(schoolRepository.searchByQuery(any(), any(Pageable.class)))
                .thenReturn(page);

        // when
        SchoolListResponse response = schoolService.getSchoolsByQuery("위시", pageable);

        // then
        assertThat(response.getSchools()).hasSize(1);
        assertThat(response.getSchools().get(0).getSchoolName()).isEqualTo("위시고등학교");
        assertThat(response.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("검색어 기반 조회 시 404 에러")
    void getSchoolsByQuery_throwsException_whenEmpty(){
        // given
        Page<School> page = new PageImpl<>(List.of(mockSchool), pageable, 1);

        when(schoolRepository.searchByQuery(any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        // when & then
        GoGildongException exception = assertThrows(
                GoGildongException.class,
                () -> schoolService.getSchoolsByQuery("서울", pageable)
        );
        // 예외 메시지 확인
        assertThat(exception.getMessage()).isEqualTo(ExceptionCode.SCHOOL_NOT_FOUND.getMessage());
    }

}