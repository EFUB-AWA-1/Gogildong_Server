package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository <Facility, Long> {
}
