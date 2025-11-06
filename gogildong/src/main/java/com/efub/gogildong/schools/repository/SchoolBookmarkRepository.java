package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolBookmark;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolBookmarkRepository extends JpaRepository<SchoolBookmark, Long> {
    boolean existsBySchoolAndUser(School school, User user);
    Optional<SchoolBookmark> findBySchoolAndUser(School school, User user);
}
