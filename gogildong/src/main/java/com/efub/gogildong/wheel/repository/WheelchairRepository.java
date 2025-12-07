package com.efub.gogildong.wheel.repository;

import com.efub.gogildong.wheel.domain.Wheelchair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WheelchairRepository extends JpaRepository<Wheelchair, Long> {

    Page<Wheelchair> findByWheelchairNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Wheelchair> findTop5ByOrderByBookmarkCountDesc();
}
