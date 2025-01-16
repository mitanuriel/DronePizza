package com.example.dronepizza.controller;

import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getPendingDeliveries() {
        List<Delivery> deliveries = deliveryService.getPendingDeliveries();
        return ResponseEntity.ok(deliveries);
    }


    @PostMapping("/add/{pizzaId}")
    public ResponseEntity<String> addDelivery(@PathVariable Long pizzaId) {
        try {
            deliveryService.addDelivery(pizzaId);
            return ResponseEntity.ok("Delivery successfully added.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
