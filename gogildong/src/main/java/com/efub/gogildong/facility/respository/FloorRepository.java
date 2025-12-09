package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByBuilding(Building building);

    Optional<Floor> findByFloorId(Long floorId);
    Optional<Floor> findByBuildingAndFloorName(Building building, String floorName);
}
