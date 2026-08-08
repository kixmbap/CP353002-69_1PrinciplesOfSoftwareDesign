package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("NONE")
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice;
    }

    @Override
    public String getDiscountName() {
        return "ราคาปกติ (0%)";
    }
}
