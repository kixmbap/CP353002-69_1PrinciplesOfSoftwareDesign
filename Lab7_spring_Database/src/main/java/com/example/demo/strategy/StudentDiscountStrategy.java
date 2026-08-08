package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("STUDENT")
public class StudentDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice * 0.90; // 10% discount
    }

    @Override
    public String getDiscountName() {
        return "ส่วนลดนักศึกษา (10%)";
    }
}
