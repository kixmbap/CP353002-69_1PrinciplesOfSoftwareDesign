package com.example.demo.strategy;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DiscountContext {

    private final Map<String, DiscountStrategy> strategies;

    // Spring injects strategies keyed by their component names: NONE, MEMBER, SEASONAL.
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
        return getStrategy(discountType).calculateFinalPrice(originalPrice);
    }

    public String getDiscountName(String discountType) {
        return getStrategy(discountType).getDiscountName();
    }
}
