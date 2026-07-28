package com.example.coffeemenu.service;

import com.example.coffeemenu.model.Coffee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Coffee> getCoffeeById(Long id) {
        return coffees.stream()
                .filter(coffee -> coffee.getId().equals(id))
                .findFirst();
    }

    public Coffee addCoffee(Coffee coffee) {
        Long nextId = coffees.stream()
                .map(Coffee::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;

        Coffee newCoffee = new Coffee(nextId, coffee.getName(), coffee.getPrice());
        coffees.add(newCoffee);
        return newCoffee;
    }

    public Optional<Coffee> updateCoffee(Long id, Coffee coffee) {
        for (Coffee currentCoffee : coffees) {
            if (currentCoffee.getId().equals(id)) {
                currentCoffee.setName(coffee.getName());
                currentCoffee.setPrice(coffee.getPrice());
                return Optional.of(currentCoffee);
            }
        }
        return Optional.empty();
    }

    public boolean deleteCoffee(Long id) {
        return coffees.removeIf(coffee -> coffee.getId().equals(id));
    }
}
