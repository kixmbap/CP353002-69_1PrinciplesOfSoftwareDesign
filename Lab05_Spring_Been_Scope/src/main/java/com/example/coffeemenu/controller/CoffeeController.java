package com.example.coffeemenu.controller;

import com.example.coffeemenu.model.Coffee;
import com.example.coffeemenu.service.CoffeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public List<Coffee> getAllCoffees() {
        return coffeeService.getAllCoffees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coffee> getCoffeeById(@PathVariable Long id) {
        return coffeeService.getCoffeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Coffee> addCoffee(@RequestBody Coffee coffee) {
        Coffee createdCoffee = coffeeService.addCoffee(coffee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoffee);
    }
}
