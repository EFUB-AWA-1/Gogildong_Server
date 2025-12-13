package com.efub.gogildong.schools.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAddSchoolRequest {

    private String schoolCode;
    private String schoolName;
    private String address;

    // GeoJSON 형태
    private Location location;

    private String eduLevel;
    private String adminCode;
    private Boolean hasSpecialClass;
    private String region;

    @Getter
    @Setter
    public static class Location {
        private String type; // "Point"
        private double[] coordinates; // [lon, lat]
    }
}
