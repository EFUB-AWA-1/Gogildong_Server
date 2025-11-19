package com.efub.gogildong.reports.repository;

import com.efub.gogildong.reports.domain.ReportFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportFlagRepository extends JpaRepository<ReportFlag, Long> {
    List<ReportFlag> findAllByReport_ReportId(Long reportId);
}
