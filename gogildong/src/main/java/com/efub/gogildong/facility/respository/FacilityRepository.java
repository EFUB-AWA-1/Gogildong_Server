package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository <Facility, Long> {
    Optional<Facility> findByFacilityId(Long facilityId);
}
