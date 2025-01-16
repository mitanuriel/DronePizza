package com.example.dronepizza.service;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.model.Pizza;
import com.example.dronepizza.repositories.DroneRepository;
import com.example.dronepizza.repositories.PizzaRepository;
import org.springframework.stereotype.Service;
import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.repositories.DeliveryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final PizzaRepository pizzaRepository;
    private final DroneRepository droneRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, PizzaRepository pizzaRepository, DroneRepository droneRepository) {
        this.deliveryRepository = deliveryRepository;
        this.pizzaRepository = pizzaRepository;
        this.droneRepository = droneRepository;
    }

    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByActualDeliveryIsNull();
    }

    public void addDelivery(Long pizzaId){
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza not found with ID: " + pizzaId));

        Delivery delivery = new Delivery();
        delivery.setPizza(pizza);
        delivery.setExpectedDelivery(LocalDateTime.now().plusMinutes(30));
        delivery.setActualDelivery(null);
        delivery.setAddress("Default Address");
        delivery.setDrone(null);

        deliveryRepository.save(delivery);
    }


    public List<Delivery> getQueuedDeliveries() {
        return deliveryRepository.findByDroneIsNull();
    }

    public void scheduleDelivery(Long deliveryId, Long droneId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + deliveryId));

        Drone drone = (droneId != null)
                ? droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("Drone not found with ID: " + droneId))
                : droneRepository.findFirstByOperationalStatus(OperationalStatus.IN_OPERATION)
                .orElseThrow(() -> new IllegalStateException("No available drones in operation!"));

        if (!OperationalStatus.IN_OPERATION.equals(drone.getOperationalStatus())) {
            throw new IllegalStateException("Drone is not in operation!");
        }

        delivery.setDrone(drone);
        deliveryRepository.save(delivery);
    }



    public void finishDelivery(Long deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + deliveryId));

        if (delivery.getDrone() == null) {
            throw new IllegalStateException("Delivery does not have a drone assigned!");
        }


        delivery.setActualDelivery(LocalDateTime.now());
        deliveryRepository.save(delivery);
    }


    public List<Delivery> getUndeliveredDeliveries() {
        return deliveryRepository.findAllUndelivered();
    }



}
