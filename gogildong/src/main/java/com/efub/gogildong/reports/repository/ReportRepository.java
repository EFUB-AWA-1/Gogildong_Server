package com.efub.gogildong.reports.repository;

import com.efub.gogildong.reports.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
