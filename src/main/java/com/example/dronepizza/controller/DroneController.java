package com.example.dronepizza.controller;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drones")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService){
        this.droneService = droneService;
    }

    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        List<Drone> drones = droneService.getAllDrones();
        return ResponseEntity.ok(drones);
    }


    @GetMapping("/uuids")
    public ResponseEntity<List<String>> getAllUuids() {
        List<String> uuids = droneService.getAllUuids();
        return ResponseEntity.ok(uuids);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Drone>> getDronesByOperationalStatus(@PathVariable OperationalStatus status) {
        List<Drone> drones = droneService.getDronesByOperationalStatus(status);
        return ResponseEntity.ok(drones);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDrone() {
        try {
            droneService.addDrone();
            return ResponseEntity.ok("Drone successfully created.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}