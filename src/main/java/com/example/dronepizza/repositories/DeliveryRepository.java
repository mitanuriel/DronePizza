package com.example.dronepizza.repositories;
import com.example.dronepizza.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery> findByActualDeliveryIsNull();

    List<Delivery> findByDroneIsNull();

    @Query("SELECT d FROM Delivery d WHERE d.actualDelivery IS NULL ORDER BY d.expectedDelivery ASC")
    List<Delivery> findAllUndelivered();




}