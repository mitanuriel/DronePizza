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

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setOperationalStatus(OperationalStatus.RETIRED);


        when(mockDroneRepository.findById(1L)).thenReturn(Optional.of(drone));

        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);


        droneService.enableDrone(1L);

        assertEquals(OperationalStatus.IN_OPERATION, drone.getOperationalStatus());
        verify(mockDroneRepository, times(1)).save(drone);
    }

    @Test
    void enableDrone_throwsExceptionIfDroneNotFound() {

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        when(mockDroneRepository.findById(99L)).thenReturn(Optional.empty());

        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            droneService.enableDrone(99L);
        });
        assertEquals("Drone not found with ID: 99", exception.getMessage());
    }

    @Test
    void disableDrone_updatesStatusToOutOfOperation() {

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);


        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setOperationalStatus(OperationalStatus.IN_OPERATION);

        when(mockDroneRepository.findById(1L)).thenReturn(Optional.of(drone));


        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);


        droneService.disableDrone(1L);


        assertEquals(OperationalStatus.OUT_OF_OPERATION, drone.getOperationalStatus());
        verify(mockDroneRepository, times(1)).save(drone);
    }

    @Test
    void disableDrone_throwsExceptionIfDroneNotFound() {

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        when(mockDroneRepository.findById(99L)).thenReturn(Optional.empty());

        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            droneService.disableDrone(99L);
        });
        assertEquals("Drone not found with ID: 99", exception.getMessage());
    }

    @Test
    void retireDrone_updatesStatusToRetired() {

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setOperationalStatus(OperationalStatus.IN_OPERATION);

        when(mockDroneRepository.findById(1L)).thenReturn(Optional.of(drone));

        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);

        droneService.retireDrone(1L);

        assertEquals(OperationalStatus.RETIRED, drone.getOperationalStatus());
        verify(mockDroneRepository, times(1)).save(drone);
    }

    @Test
    void retireDrone_throwsExceptionIfDroneNotFound() {

        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        StationRepository mockStationRepository = mock(StationRepository.class);

        when(mockDroneRepository.findById(99L)).thenReturn(Optional.empty());

        DroneService droneService = new DroneService(mockDroneRepository, mockStationRepository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            droneService.retireDrone(99L);
        });
        assertEquals("Drone not found with ID: 99", exception.getMessage());
    }

}