package com.efub.gogildong.reports.repository;

import com.efub.gogildong.facility.domain.Etc;
import com.efub.gogildong.reports.domain.EtcReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtcReportRepository extends JpaRepository<EtcReport, Long> {
    @Query("""
            SELECT r FROM EtcReport r 
            WHERE r.etc = :etc 
              AND r.report.isPublic = true
           """)
    List<EtcReport> findPublicByEtc(@Param("etc") Etc etc);
}
