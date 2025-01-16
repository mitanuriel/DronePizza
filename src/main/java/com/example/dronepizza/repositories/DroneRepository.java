package com.example.dronepizza.repositories;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    @Query("SELECT d.serialUuid FROM Drone d")
    List<String> findAllUuids();


    List<Drone> findByOperationalStatus(OperationalStatus operationalStatus);
}
