package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolViewRequestRepository extends JpaRepository<SchoolViewRequest, Long> {
    Optional<SchoolViewRequest> findBySchoolAndUser(School school, User user);
}
