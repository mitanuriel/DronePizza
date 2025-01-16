package com.example.dronepizza.model;
import jakarta.persistence.*;

@Entity
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long droneId;

    @Column(unique = true, nullable = false)
    private String serialUuid;

    @Enumerated(EnumType.STRING)
    private OperationalStatus operationalStatus;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;



    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }


    public String getSerialUuid() {
        return serialUuid;
    }

    public void setSerialUuid(String serialUuid) {
        this.serialUuid = serialUuid;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

}