package com.efub.gogildong.reports.repository;

import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.reports.domain.ReportFlag;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportFlagRepository extends JpaRepository<ReportFlag, Long> {
    List<ReportFlag> findAllByReport_ReportId(Long reportId);
    boolean existsByUserAndReport(User user, Report report);
}
