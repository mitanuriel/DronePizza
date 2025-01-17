package com.example.dronepizza.controller;

import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
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
            System.out.println("Pizza ID received: " + pizzaId); // Log the pizzaId
            deliveryService.addDelivery(pizzaId);
            return ResponseEntity.ok("Delivery successfully added.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding delivery: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/queue")
    public ResponseEntity<List<Delivery>> getQueuedDeliveries() {
        List<Delivery> queuedDeliveries = deliveryService.getQueuedDeliveries();
        return ResponseEntity.ok(queuedDeliveries);
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleDelivery(@RequestBody Map<String, Object> requestBody) {
        try {
            // Log the received request body
            System.out.println("Request Body: " + requestBody);

            Long deliveryId = Long.valueOf(requestBody.get("deliveryId").toString());
            Long droneId = requestBody.get("droneId") != null
                    ? Long.valueOf(requestBody.get("droneId").toString())
                    : null;

            // Log extracted values
            System.out.println("Delivery ID: " + deliveryId);
            System.out.println("Drone ID: " + droneId);

            deliveryService.scheduleDelivery(deliveryId, droneId);
            return ResponseEntity.ok("Delivery scheduled successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());


        }
    }


    @PostMapping("/finish")
    public ResponseEntity<String> finishDelivery(@RequestParam Long deliveryId) {
        try {
            deliveryService.finishDelivery(deliveryId);
            return ResponseEntity.ok("Delivery finished successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/finish2")
    public ResponseEntity<String> finishDelivery2(@RequestParam Long deliveryId) {
        try {
            deliveryService.finishDelivery(deliveryId);
            return ResponseEntity.ok("Delivery completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/undelivered")
    public ResponseEntity<List<Delivery>> getUndeliveredDeliveries() {
        List<Delivery> deliveries = deliveryService.getUndeliveredDeliveries();
        return ResponseEntity.ok(deliveries);
    }


}
