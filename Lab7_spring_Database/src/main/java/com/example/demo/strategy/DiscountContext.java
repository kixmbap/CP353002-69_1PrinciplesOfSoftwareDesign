package com.example.demo.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiscountContext {

    private final Map<String, DiscountStrategy> strategies;

    @Autowired
    public DiscountContext(Map<String, DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public DiscountStrategy getStrategy(String discountType) {
        if (discountType == null || !strategies.containsKey(discountType)) {
            return strategies.get("NONE");
        }
        return strategies.get(discountType);
    }

    public double calculateFinalPrice(String discountType, double originalPrice) {
        DiscountStrategy strategy = getStrategy(discountType);
        return strategy.calculateFinalPrice(originalPrice);
    }

    public String getDiscountName(String discountType) {
        DiscountStrategy strategy = getStrategy(discountType);
        return strategy.getDiscountName();
    }
}
