package com.example.dronepizza.service;
import com.example.dronepizza.model.Drone;
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



}