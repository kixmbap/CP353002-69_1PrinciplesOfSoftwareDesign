package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// ══════════════════════════════════════════════════════
//  Test Cases — Section 2 : FlashMove Express
//  รันด้วย:  mvn test
//            mvn -Dtest=ShipmentTest_Section2 test
// ══════════════════════════════════════════════════════
@DisplayName("Section 2 — FlashMove Express")
class ShipmentTest_Section2 {

    @Test
    @DisplayName("FM001: 4.0 กก. STANDARD ต้องได้ 160.00 บาท")
    void sec2_standard_4kg() {
        Shipment s = new Shipment("FM001", 4.0, ShipmentType.STANDARD);
        assertEquals(160.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("FM002: 2.5 กก. EXPRESS ต้องได้ 250.00 บาท")
    void sec2_express_2_5kg() {
        Shipment s = new Shipment("FM002", 2.5, ShipmentType.EXPRESS);
        assertEquals(250.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("FM003: 6.0 กก. STANDARD ต้องได้ 240.00 บาท")
    void sec2_standard_6kg() {
        Shipment s = new Shipment("FM003", 6.0, ShipmentType.STANDARD);
        assertEquals(240.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("FM004: 1.0 กก. EXPRESS ต้องได้ 100.00 บาท")
    void sec2_express_1kg() {
        Shipment s = new Shipment("FM004", 1.0, ShipmentType.EXPRESS);
        assertEquals(100.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("FM005: 8.0 กก. STANDARD ต้องได้ 320.00 บาท")
    void sec2_standard_8kg() {
        Shipment s = new Shipment("FM005", 8.0, ShipmentType.STANDARD);
        assertEquals(320.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("ยอดรวม FlashMove ต้องได้ 1,070.00 บาท")
    void sec2_totalCost() {
        ShippingCompany company = new ShippingCompany("FlashMove Express");
        company.addShipment(new Shipment("FM001", 4.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("FM002", 2.5, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("FM003", 6.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("FM004", 1.0, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("FM005", 8.0, ShipmentType.STANDARD));
        assertEquals(1070.00, company.getTotalCost(), 0.01);
    }

    @Test
    @DisplayName("[EDGE] ไม่มี Shipment ยอดรวมต้องเป็น 0")
    void sec2_emptyCompany() {
        ShippingCompany company = new ShippingCompany("FlashMove Express");
        assertEquals(0.00, company.getTotalCost(), 0.01);
    }
}
