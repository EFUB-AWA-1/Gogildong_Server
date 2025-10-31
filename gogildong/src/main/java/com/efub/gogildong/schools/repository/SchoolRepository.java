package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsBySchoolCode(String schoolCode);
}
