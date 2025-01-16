package com.example.dronepizza.controller;

import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
