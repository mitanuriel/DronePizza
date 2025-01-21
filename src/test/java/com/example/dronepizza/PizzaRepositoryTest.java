package com.example.dronepizza;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.dronepizza.repositories.PizzaRepository;
import com.example.dronepizza.model.Pizza;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class PizzaRepositoryTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Test
    public void testFindById() {
        Optional<Pizza> pizza = pizzaRepository.findById(1L);
        assertTrue(pizza.isPresent(), "Pizza with ID 1 should exist.");
        System.out.println(pizza.get());
    }
}