package com.example.dronepizza.service;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.model.Station;
import com.example.dronepizza.repositories.DroneRepository;
import com.example.dronepizza.repositories.StationRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final StationRepository stationRepository;

    public DroneService(DroneRepository droneRepository, StationRepository stationRepository){
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
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

    public void addDrone() {
        List<Station>stations = stationRepository.findAll();

        if(stations.isEmpty()){
            throw new IllegalStateException("There is no available stations in database");
        }

        Station selectedStation = stations.stream()
                .min(Comparator.comparingInt(station -> droneRepository.countByStation(station)))
                .orElseThrow(() -> new IllegalStateException("Failed to find a station."));

        Drone newDrone = new Drone();
        newDrone.setSerialUuid(UUID.randomUUID().toString());
        newDrone.setOperationalStatus(OperationalStatus.IN_OPERATION);
        newDrone.setStation(selectedStation);

         droneRepository.save(newDrone);
    }
}