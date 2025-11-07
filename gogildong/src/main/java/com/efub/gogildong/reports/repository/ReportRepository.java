package com.efub.gogildong.reports.repository;

import com.efub.gogildong.reports.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // 최신순으로 모든 제보 불러오기
    List<Report> findByOrderByCreatedAtDesc();
}
