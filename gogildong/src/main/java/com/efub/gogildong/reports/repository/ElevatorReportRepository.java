package com.efub.gogildong.reports.repository;

import com.efub.gogildong.facility.domain.Elevator;
import com.efub.gogildong.reports.domain.ElevatorReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ElevatorReportRepository extends JpaRepository<ElevatorReport, Long> {
    @Query("""
            SELECT r FROM ElevatorReport r 
            WHERE r.elevator = :elevator 
              AND r.report.isPublic = true
           """)
    List<ElevatorReport> findPublicByElevator(@Param("elevator")Elevator elevator);
}
