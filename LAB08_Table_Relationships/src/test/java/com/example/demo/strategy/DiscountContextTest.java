package com.example.demo.strategy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Load real strategy beans to check component names and constructor injection together.
@SpringJUnitConfig({DiscountContext.class, NoDiscountStrategy.class,
        MemberDiscountStrategy.class, SeasonalSaleStrategy.class})
class DiscountContextTest {

    @Autowired
    private DiscountContext context;

    @ParameterizedTest
    @CsvSource({
            "NONE, 1000, 1000",
            "MEMBER, 1000, 900",
            "SEASONAL, 1000, 800",
            "MEMBER, 199.50, 179.55",
            "SEASONAL, 199.50, 159.60",
            "NONE, 0, 0",
            "MEMBER, 0, 0",
            "SEASONAL, 0, 0"
    })
    void calculatesPriceForSelectedDiscount(String type, double price, double expected) {
        assertEquals(expected, context.calculateFinalPrice(type, price), 0.000001);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"UNKNOWN"})
    void fallsBackToRegularPriceForMissingOrUnknownDiscount(String type) {
        assertEquals(1000.0, context.calculateFinalPrice(type, 1000.0), 0.000001);
        assertEquals("ราคาปกติ (0%)", context.getDiscountName(type));
    }

    @ParameterizedTest
    @CsvSource({
            "NONE, ราคาปกติ (0%)",
            "MEMBER, ส่วนลดสมาชิก (10%)",
            "SEASONAL, ส่วนลดเทศกาล (20%)"
    })
    void returnsLabelForSelectedDiscount(String type, String expected) {
        assertEquals(expected, context.getDiscountName(type));
    }
}
