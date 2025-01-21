package com.example.dronepizza;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Station;
import com.example.dronepizza.repositories.DroneRepository;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.repositories.StationRepository;
import com.example.dronepizza.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private StationRepository stationRepository;

    @Test
    public void testGetAllDrones() throws Exception {
        mockMvc.perform(get("/drones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }



    @Test
    void getAllUuids_success() throws Exception {
        // Perform GET request
        mockMvc.perform(get("/drones/uuids"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getDronesByOperationalStatus_success() throws Exception {
        // Ensure a drone exists with a specific status
        Drone drone = new Drone();
        drone.setSerialUuid("test-uuid");
        drone.setOperationalStatus(OperationalStatus.IN_OPERATION);
        droneRepository.save(drone);

        // Perform GET request
        mockMvc.perform(get("/drones/status/IN_OPERATION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operationalStatus").value("IN_OPERATION"));
    }

    @Test
    void addDrone_success() throws Exception {
        // Ensure a station exists
        Station station = new Station();
        station.setLatitude(55.405);
        station.setLongitude(12.342);
        stationRepository.save(station);

        // Perform POST request
        mockMvc.perform(post("/drones/add"))
                .andExpect(status().isOk())
                .andExpect(content().string("Drone successfully created."));


    }

    @Test
    void addDrone_noStationsAvailable() throws Exception {
        // Ensure no stations are in the database
        droneRepository.deleteAll();
        stationRepository.deleteAll();

        // Perform POST request
        mockMvc.perform(post("/drones/add"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("There is no available stations in database"));
    }





}