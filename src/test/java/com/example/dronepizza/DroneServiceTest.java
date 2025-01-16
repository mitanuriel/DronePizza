package com.example.dronepizza;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.repositories.DroneRepository;
import com.example.dronepizza.repositories.StationRepository;
import com.example.dronepizza.service.DroneService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceTest {

    @Test
    void enableDrone_updatesStatusToInOperation() {
        // Mock the repository
        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        // Mock a drone
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setOperationalStatus(OperationalStatus.RETIRED);

        // Mock repository behavior
        when(mockDroneRepository.findById(1L)).thenReturn(Optional.of(drone));

        // Create the service with the mocked repository
        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);


        // Act: Enable the drone
        droneService.enableDrone(1L);

        // Assert: Check the status and save operation
        assertEquals(OperationalStatus.IN_OPERATION, drone.getOperationalStatus());
        verify(mockDroneRepository, times(1)).save(drone);
    }

    @Test
    void enableDrone_throwsExceptionIfDroneNotFound() {
        // Mock the repository
        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        // Mock repository behavior: No drone found
        when(mockDroneRepository.findById(99L)).thenReturn(Optional.empty());

        // Create the service with the mocked repository
        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);

        // Act & Assert: Expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            droneService.enableDrone(99L);
        });
        assertEquals("Drone not found with ID: 99", exception.getMessage());
    }
}