package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.schools.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findBySchool(School school);

    @Query("SELECT b FROM Building b JOIN b.floors f WHERE f = :floor")
    Optional<Building> findByFloor(@Param("floor") Floor floor);
}
