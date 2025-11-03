package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.EduLevel;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchoolDataLoader {

    private final SchoolRepository schoolRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private static final int BATCH_SIZE = 1000;
    private static final String K12_FILE = "K12_School_Location_Standard_Data.csv";
    private static final String UNI_FILE = "Uni_Location_Standard_Data.xlsx";

    /*
    * 학교 데이터를 데이터베이스에 삽입합니다.
    * */
    @PostConstruct
    public void loadSchoolsData() throws Exception{
        // 기존 데이터 미리 불러오기 (학교명 + 주소)
        Set<String> existingSchools = schoolRepository.findAll()
                .stream()
                .map(sc -> sc.getSchoolName() + "|" + sc.getAddress())
                .collect(Collectors.toSet());

        List<School> allSchools = new ArrayList<>();
        allSchools.addAll(loadK12Schools(existingSchools));
        allSchools.addAll(loadUni(existingSchools));

        // 배치로 저장
        saveInBatches(allSchools);
    }

    /*
    * 초/중/고 데이터를 삽입합니다.
    * */
    private List<School> loadK12Schools(Set<String> existingSchools) throws Exception{
        List<School> result = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/" + K12_FILE);
             CSVReader reader = new CSVReader(new InputStreamReader(is, "MS949"))) {

            reader.readNext(); // 헤더 스킵
            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[1];
                String address = line[8];
                if (existingSchools.contains(name + "|" + address)) continue; // 이미 포함된 경우 스킵
                EduLevel eduLevel = getEdulevel(line[2]);
                Point location = toPoint(line[16], line[15]);
                School newSchool = School.builder()
                        .schoolCode(generateUniqueSchoolCode())
                        .schoolName(name)
                        .address(address)
                        .location(location)
                        .eduLevel(eduLevel)
                        .build();
                result.add(newSchool);
            }
        }
        return result;
    }

    /*
    * 대학 데이터를 삽입합니다.
    * */
    private List<School> loadUni(Set<String> existingSchools) throws Exception{
        List<School> result = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/" + UNI_FILE);
             CSVReader reader = new CSVReader(new InputStreamReader(is, "utf-8"))) {
            String [] line;
            reader.readNext();
            while((line = reader.readNext()) != null) {
                String uniType = line[0];
                if(uniType.contains("대학원")) continue; // 대학원 제회
                String uniName = line[2];
                String campus = line[3];
                if (!campus.equals("본교") && !campus.equals("분교")) { // 캠퍼스 이름 포함
                    uniName += " " + campus;
                }
                String address = line[12];
                if(existingSchools.contains(uniName + "|" + address)) continue; // 이미 포함된 경우 제외

                Point location = toPoint(line[15], line[14]);
                School newSchool = School.builder()
                        .schoolCode(generateUniqueSchoolCode())
                        .schoolName(uniName)
                        .address(address)
                        .location(location)
                        .eduLevel(EduLevel.uni)
                        .build();
                result.add(newSchool);
            }
        }
        return result;
    }

    /*
    * 초/중/고로 학교를 구분합니다.
    * */
    private EduLevel getEdulevel(String line) {
        if(line.equals("초등학교"))
            return EduLevel.primary;
        else if(line.equals("중학교"))
            return EduLevel.middle;
        else if(line.equals("고등학교"))
            return EduLevel.high;
        throw new GoGildongException(ExceptionCode.EDULEVEL_NOT_FOUND);
    }

    /*
    * 위도, 경도를 바탕으로 point를 생성합니다.
    * */
    private Point toPoint(String lon, String lat) {
        double longitude = Double.parseDouble(lon);
        double latitude = Double.parseDouble(lat);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    /*
    * 배치 저장
    * */
    private void saveInBatches(List<School> schools) {
        for (int i = 0; i < schools.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, schools.size());
            schoolRepository.saveAll(schools.subList(i, end));
        }
    }

    /*
    * UUID 기반의 중복 없는 8자리 학교 코드를 생성합니다.
    * */
    private String generateUniqueSchoolCode() {
        String code;
        do{
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        } while (schoolRepository.existsBySchoolCode(code));
        return code;
    }
}
