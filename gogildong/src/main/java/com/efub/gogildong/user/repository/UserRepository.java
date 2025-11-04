package com.efub.gogildong.user.repository;

import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // userId로 조회
    Optional<User> findByUserId(Long userId);
}
