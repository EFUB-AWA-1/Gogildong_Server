package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.Etc;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class EtcReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long etcReportId;

    @Column(nullable = false)
    private String etcReportImage;

    @Column(nullable = false)
    private String note;

    @OneToOne
    @Setter
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "etc_id", nullable = false)
    @Setter
    private Etc etc;

    @Builder
    public EtcReport(String etcReportImage, String note, Report report, Etc etc) {
        this.etcReportImage = etcReportImage;
        this.note = note;
        this.report = report;
        this.etc = etc;
    }
}
