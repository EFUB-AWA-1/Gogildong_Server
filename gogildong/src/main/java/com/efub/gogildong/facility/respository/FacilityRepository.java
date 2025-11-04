package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository <Facility, Long> {
    Optional<Facility> findByFacilityId(Long facilityId);
}
