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

    public void addDelivery(Long pizzaId) {
        // Find the pizza by ID
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pizza not found with ID: " + pizzaId));

        // Log the pizza details
        System.out.println("Pizza found: " + pizza);

        // Create a new delivery object
        Delivery delivery = new Delivery();
        delivery.setPizza(pizza); // Associate the pizza with the delivery
        delivery.setStatus("PENDING"); // Set status as pending

        // Save the delivery to the database
        deliveryRepository.save(delivery);

        // Log the saved delivery
        System.out.println("Delivery created: " + delivery);
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

    public void finishDelivery2(Long deliveryId) {
        // Find the delivery by ID
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with ID: " + deliveryId));

        // Update the delivery status to "COMPLETED"
        delivery.setStatus("COMPLETED");

        // Save the updated delivery
        deliveryRepository.save(delivery);
    }



    public List<Delivery> getUndeliveredDeliveries() {
        return deliveryRepository.findAllUndelivered();
    }



}
