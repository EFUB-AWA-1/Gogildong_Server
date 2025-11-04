package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.SchoolViewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolViewRequestRepository extends JpaRepository<SchoolViewRequest, Long> {
}
