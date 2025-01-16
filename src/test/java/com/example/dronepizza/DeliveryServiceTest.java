package com.example.dronepizza;

import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.OperationalStatus;
import com.example.dronepizza.model.Pizza;
import com.example.dronepizza.repositories.DeliveryRepository;
import com.example.dronepizza.repositories.DroneRepository;
import com.example.dronepizza.repositories.PizzaRepository;
import com.example.dronepizza.service.DeliveryService;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;




import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


public class DeliveryServiceTest {

    @Test
    void getPendingDeliveries_returnsPendingDeliveries() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);

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

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository, mockPizzaRepository,
                mockDroneRepository);

        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();

        assertEquals(2, pendingDeliveries.size());
        assertEquals("123 Main Street", pendingDeliveries.get(0).getAddress());
        assertEquals("789 Pine Lane", pendingDeliveries.get(1).getAddress());
        verify(mockDeliveryRepository, times(1)).findByActualDeliveryIsNull();
    }

    @Test
    void getPendingDeliveries_returnsEmptyListIfNoPendingDeliveries() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);


        when(mockDeliveryRepository.findByActualDeliveryIsNull()).thenReturn(Arrays.asList());

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository, mockPizzaRepository,
                mockDroneRepository);
        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();

        assertEquals(0, pendingDeliveries.size());
        verify(mockDeliveryRepository, times(1)).findByActualDeliveryIsNull();
    }

    @Test
    void addDelivery_createsNewDeliverySuccessfully() {
        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository, mockPizzaRepository,
                mockDroneRepository);

        Pizza pizza = new Pizza();
        pizza.setPizzaId(1L);


        when(mockPizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));

        final Delivery[] savedDelivery = new Delivery[1];
        doAnswer(invocation -> {
            savedDelivery[0] = invocation.getArgument(0);
            return null;
        }).when(mockDeliveryRepository).save(any(Delivery.class));

        deliveryService.addDelivery(1L);


        assertNotNull(savedDelivery[0]);
        assertEquals(pizza, savedDelivery[0].getPizza());
        assertNotNull(savedDelivery[0].getExpectedDelivery());
        assertNull(savedDelivery[0].getActualDelivery());
    }


    @Test
    void getQueuedDeliveries_returnsDeliveriesWithoutDrones() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);

        Delivery delivery1 = new Delivery();
        delivery1.setDeliveryId(1L);
        delivery1.setAddress("123 Main Street");

        Delivery delivery2 = new Delivery();
        delivery2.setDeliveryId(2L);
        delivery2.setAddress("789 Pine Lane");

        when(mockDeliveryRepository.findByDroneIsNull())
                .thenReturn(Arrays.asList(delivery1, delivery2));


        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository, mockPizzaRepository, mockDroneRepository);

        List<Delivery> queuedDeliveries = deliveryService.getQueuedDeliveries();

        assertEquals(2, queuedDeliveries.size());
        assertEquals("123 Main Street", queuedDeliveries.get(0).getAddress());
        assertEquals("789 Pine Lane", queuedDeliveries.get(1).getAddress());

        verify(mockDeliveryRepository, times(1)).findByDroneIsNull();
    }

    @Test
    void scheduleDelivery_success() {
        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);


        Delivery delivery = new Delivery();
        delivery.setDeliveryId(1L);
        delivery.setDrone(null);

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setOperationalStatus(OperationalStatus.IN_OPERATION);

        when(mockDeliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(mockDroneRepository.findById(1L)).thenReturn(Optional.of(drone));

        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository, mockPizzaRepository,
                mockDroneRepository);


        deliveryService.scheduleDelivery(1L, 1L);

        assertEquals(drone, delivery.getDrone());
        verify(mockDeliveryRepository, times(1)).save(delivery);
    }

    @Test
    void scheduleDelivery_throwsExceptionIfDeliveryAlreadyAssigned() {

        DeliveryRepository mockDeliveryRepository = mock(DeliveryRepository.class);
        DroneRepository mockDroneRepository = mock(DroneRepository.class);
        PizzaRepository mockPizzaRepository = mock(PizzaRepository.class);


        Delivery delivery = new Delivery();
        delivery.setDeliveryId(1L);
        Drone existingDrone = new Drone();
        existingDrone.setDroneId(2L);
        delivery.setDrone(existingDrone);

        when(mockDeliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));


        DeliveryService deliveryService = new DeliveryService(mockDeliveryRepository,mockPizzaRepository,
                mockDroneRepository);


        Exception exception = assertThrows(IllegalStateException.class, () -> {
            deliveryService.scheduleDelivery(1L, 1L);
        });
        assertEquals("Delivery already has a drone assigned!", exception.getMessage());
    }
}


