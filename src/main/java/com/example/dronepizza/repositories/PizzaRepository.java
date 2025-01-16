package com.example.dronepizza.repositories;
import com.example.dronepizza.model.Delivery;
import com.example.dronepizza.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza,Long> {

}

