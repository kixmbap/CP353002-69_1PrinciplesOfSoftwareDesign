package com.example.demo.strategy;

public interface DiscountStrategy {
    double calculateFinalPrice(double originalPrice);
    String getDiscountName();
}
