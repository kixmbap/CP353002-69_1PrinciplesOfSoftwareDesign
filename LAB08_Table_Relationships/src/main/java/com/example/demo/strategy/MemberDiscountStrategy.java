package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("MEMBER")
public class MemberDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice * 0.90;
    }

    @Override
    public String getDiscountName() {
        return "ส่วนลดสมาชิก (10%)";
    }
}
