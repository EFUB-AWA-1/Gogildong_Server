package com.efub.gogildong.reports.repository;

import com.efub.gogildong.facility.domain.Classroom;
import com.efub.gogildong.reports.domain.ClassroomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomReportRepository extends JpaRepository<ClassroomReport, Long> {
    @Query("""
            SELECT r FROM ClassroomReport r 
            WHERE r.classroom = :classroom 
              AND r.report.isPublic = true
           """)
    List<ClassroomReport> findPublicByClassroom(@Param("classroom")Classroom classroom);

    ClassroomReport findByReport_reportId(Long reportId);
}
