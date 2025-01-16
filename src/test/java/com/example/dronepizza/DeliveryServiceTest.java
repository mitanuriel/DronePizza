package com.example.dronepizza;

import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.repositories.DeliveryRepository;
import com.example.dronepizza.service.DeliveryService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class DeliveryServiceTest {

    @Test
    void getPendingDeliveries_returnsPendingDeliveries() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);

        Delivery delivery1 = new Delivery();
        delivery1.setDeliveryId(1L);
        delivery1.setAddress("123 Main Street");
        delivery1.setExpectedDelivery(LocalDateTime.of(2025, 1, 16, 15, 0));
        delivery1.setActualDelivery(null);

        Delivery delivery2 = new Delivery();
        delivery2.setDeliveryId(3L);
        delivery2.setAddress("789 Pine Lane");
        delivery2.setExpectedDelivery(LocalDateTime.of(2025, 1, 16, 20, 0));
        delivery2.setActualDelivery(null);

        when(mockDeliveryRepository.findByActualDeliveryIsNull()).thenReturn(Arrays.asList(delivery1, delivery2));

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository);

        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();

        assertEquals(2, pendingDeliveries.size());
        assertEquals("123 Main Street", pendingDeliveries.get(0).getAddress());
        assertEquals("789 Pine Lane", pendingDeliveries.get(1).getAddress());
        verify(mockDeliveryRepository, times(1)).findByActualDeliveryIsNull();
    }

    @Test
    void getPendingDeliveries_returnsEmptyListIfNoPendingDeliveries() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);

        when(mockDeliveryRepository.findByActualDeliveryIsNull()).thenReturn(Arrays.asList());

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository);
        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();

        assertEquals(0, pendingDeliveries.size());
        verify(mockDeliveryRepository, times(1)).findByActualDeliveryIsNull();
    }


}
