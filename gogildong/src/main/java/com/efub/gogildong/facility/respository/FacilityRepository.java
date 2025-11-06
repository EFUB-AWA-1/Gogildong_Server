package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository <Facility, Long> {

    @Query("SELECT f FROM Facility f WHERE f.floor = :floor AND (:type = 'all' OR f.facilityType = :type)")
    List<Facility> findAllByFloorAndType(@Param("floor") Floor floor, @Param("type") String type);

    Optional<Facility> findByFacilityId(Long facilityId);

    Long countByFloor(Floor floor);
}
