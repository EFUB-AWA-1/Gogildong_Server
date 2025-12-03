package com.efub.gogildong.reports.dto.request.etc;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.reports.domain.EtcReport;
import com.efub.gogildong.reports.domain.Report;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEtcReportRequest {
    private Long floorId;

    @NotBlank(message = "시설 이름을 작성해주세요.")
    private String facilityName;

    @NotBlank(message = "시설 사진을 포함해주세요!")
    private String etcReportImage;

    @NotBlank(message = "제보에 대해 설명해주세요.")
    private String note;

    public static Facility toFacilityEntity(NewEtcReportRequest request, String facilityName, Floor floor) {
        return Facility.builder()
                .facilityName(facilityName)
                .facilityNickname(request.getFacilityName())
                .facilityType(FacilityType.ETC)
                .floor(floor)
                .build();
    }

    public static EtcReport toEtcReportEntity(NewEtcReportRequest request, Report report) {
        return EtcReport.builder()
                .etcReportImage(request.getEtcReportImage())
                .note(request.getNote())
                .build();
    }

    public static Etc toEtcEntity(NewEtcReportRequest request, Facility facility, EtcReport report) {
        Etc etc = Etc.builder()
                .note(request.getNote())
                .facility(facility)
                .build();

        etc.addEtcReport(report);

        return etc;
    }
}
