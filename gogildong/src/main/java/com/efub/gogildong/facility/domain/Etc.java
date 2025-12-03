package com.efub.gogildong.facility.domain;

import com.efub.gogildong.reports.domain.EtcReport;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Etc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long etcId;

    @Column(nullable = false)
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtcReport> etcReports = new ArrayList<>();

    @Builder
    public Etc(String note, Facility facility) {
        this.note = note;
        this.facility = facility;
    }

    // 기타 제보 추가
    public void addEtcReport(EtcReport etcReport) {
        this.etcReports.add(etcReport);
        etcReport.setEtc(this);
    }
}
