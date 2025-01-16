package com.example.dronepizza.service;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.repositories.DroneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository){
        this.droneRepository = droneRepository;
    }

    public List<Drone> getAllDrones(){
        return droneRepository.findAll();
    }

    public List<String> getAllUuids() {
        return droneRepository.findAllUuids();
    }

    public List<Drone> getDronesByOperationalStatus(OperationalStatus status) {
        return droneRepository.findByOperationalStatus(status);
    }

}