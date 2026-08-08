package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("SEASONAL")
public class SeasonalSaleStrategy implements DiscountStrategy {

    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice * 0.80; // 20% discount
    }

    @Override
    public String getDiscountName() {
        return "ส่วนลดเทศกาล (20%)";
    }
}
