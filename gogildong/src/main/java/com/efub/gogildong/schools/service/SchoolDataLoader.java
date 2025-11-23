package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.EduLevel;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SchoolDataLoader {

    private final SchoolRepository schoolRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private static final int BATCH_SIZE = 1000;

    private static final String ELEMENT_FILE = "학교기본정보(초)_특수학급추가.csv";
    private static final String MIDDLE_FILE = "학교기본정보(중)_특수학급추가.csv";
    private static final String HIGH_FILE = "학교기본정보(고)_특수학급추가.csv";
    private static final String SPECIAL_FILE = "학교기본정보(특)_전체.csv";
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
        allSchools.addAll(loadK12Schools(existingSchools, ELEMENT_FILE, EduLevel.primary));
        allSchools.addAll(loadK12Schools(existingSchools, MIDDLE_FILE, EduLevel.middle));
        allSchools.addAll(loadK12Schools(existingSchools, HIGH_FILE, EduLevel.high));
        allSchools.addAll(loadSpecialSchools(existingSchools));
        allSchools.addAll(loadUni(existingSchools));

        // 배치로 저장
        saveInBatches(allSchools);
    }

    /*
     * 초/중/고등학교 데이터를 삽입합니다.
     * */
    private List<School> loadK12Schools(Set<String> existingSchools, String fileName, EduLevel eduLevel) throws Exception{
        List<School> result = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/" + fileName);
             CSVReader reader = new CSVReader(new InputStreamReader(is, "UTF-8"))) {

            reader.readNext(); // 헤더 스킵
            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[4];
                String address = line[18];
                if(address == null || address.isEmpty()){
                    address = line[15] + " " + line[16];
                }
                if (existingSchools.contains(name + "|" + address)) continue; // 이미 포함된 경우 스킵
                if (line[21].isBlank() || line[20].isBlank()) continue;
                Point location = toPoint(line[21], line[20]);
                Boolean hasSpecial = line[29].equals("Y");
                School newSchool = School.builder()
                        .schoolCode(generateUniqueSchoolCode())
                        .schoolName(name)
                        .address(address)
                        .location(location)
                        .eduLevel(eduLevel)
                        .hasSpecialClass(hasSpecial)
                        .region(fromAddress(address))
                        .build();
                result.add(newSchool);
            }
        }
        return result;
    }

    private List<School> loadSpecialSchools(Set<String> existingSchools) throws Exception{
        List<School> result = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/" + SPECIAL_FILE);
             CSVReader reader = new CSVReader(new InputStreamReader(is, "UTF-8"))) {

            reader.readNext(); // 헤더 스킵
            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[4];
                String address = line[18];
                if(address == null || address.isEmpty()){
                    address = line[15] + " " + line[16];
                }
                if (existingSchools.contains(name + "|" + address)) continue; // 이미 포함된 경우 스킵
                if (line[21].isBlank() || line[20].isBlank()) continue;
                Point location = toPoint(line[21], line[20]);

                School newSchool = School.builder()
                        .schoolCode(generateUniqueSchoolCode())
                        .schoolName(name)
                        .address(address)
                        .location(location)
                        .eduLevel(EduLevel.all)
                        .hasSpecialClass(true)
                        .region(fromAddress(address))
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
                        .hasSpecialClass(false)
                        .region(fromAddress(address))
                        .build();
                result.add(newSchool);
            }
        }
        return result;
    }

    /*
     * 위도, 경도를 바탕으로 point를 생성합니다.
     * */
    private Point toPoint(String lon, String lat) {
        double longitude = Double.parseDouble(lon);
        double latitude = Double.parseDouble(lat);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    private String fromAddress(String address) {
        String addr = address.trim();

        if (addr.startsWith("서울")) return "서울";
        if (addr.startsWith("부산")) return "부산";
        if (addr.startsWith("대구")) return "대구";
        if (addr.startsWith("인천")) return "인천";
        if (addr.startsWith("광주")) return "광주";
        if (addr.startsWith("대전")) return "대전";
        if (addr.startsWith("울산")) return "울산";
        if (addr.startsWith("세종")) return "세종";

        if (addr.startsWith("경기")) return "경기도";
        if (addr.startsWith("강원")) return "강원도";
        if (addr.startsWith("충청북도")) return "충청북도";
        if (addr.startsWith("충청남도")) return "충청남도";

        if (addr.startsWith("전북")) return "전라북도";
        if (addr.startsWith("전라남도")) return "전라남도";

        if (addr.startsWith("경상북도")) return "경상북도";
        if (addr.startsWith("경상남도")) return "경상남도";

        if (addr.startsWith("제주")) return "제주";
        log.warn("Unknown address: " + addr);
        return "알수없음";
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
