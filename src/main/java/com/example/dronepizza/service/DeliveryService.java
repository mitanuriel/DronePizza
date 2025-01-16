package com.example.dronepizza.service;

import com.example.dronepizza.model.Pizza;
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

    public DeliveryService(DeliveryRepository deliveryRepository, PizzaRepository pizzaRepository) {
        this.deliveryRepository = deliveryRepository;
        this.pizzaRepository = pizzaRepository;
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
        delivery.setActualDelivery(null); // Not yet delivered
        delivery.setAddress("Default Address"); // Placeholder, can be customized
        delivery.setDrone(null); // No drone assigned initially

        // Save the delivery
        deliveryRepository.save(delivery);
    }



}
