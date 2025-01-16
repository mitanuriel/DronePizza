package com.example.dronepizza.service;

import org.springframework.stereotype.Service;
import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.repositories.DeliveryRepository;
import java.util.List;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByActualDeliveryIsNull();
    }



}
