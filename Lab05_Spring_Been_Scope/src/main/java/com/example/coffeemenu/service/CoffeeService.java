package com.example.coffeemenu.service;

import com.example.coffeemenu.model.Coffee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoffeeService {

    private final List<Coffee> coffees = new ArrayList<>();

    public CoffeeService() {
        coffees.add(new Coffee(1L, "Espresso", 45.0));
        coffees.add(new Coffee(2L, "Latte", 55.0));
    }

    public List<Coffee> getAllCoffees() {
        return coffees;
    }
}
