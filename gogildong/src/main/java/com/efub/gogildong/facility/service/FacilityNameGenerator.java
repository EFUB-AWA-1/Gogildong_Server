package com.efub.gogildong.facility.service;

public class FacilityNameGenerator {
    public static String generateFacilityName(String floorName, Long facilityCount) {
        return floorName + "-" + toAlpha(facilityCount);
    }

    private static String toAlpha(Long facilityCount) {
        long index = facilityCount;

        StringBuilder sb = new StringBuilder();

        while (index > 0) {
            long rem = (index - 1) % 26;
            sb.append((char) ('A' + rem));
            index = (index - 1) / 26;
        }

        return sb.reverse().toString();
    }
}
