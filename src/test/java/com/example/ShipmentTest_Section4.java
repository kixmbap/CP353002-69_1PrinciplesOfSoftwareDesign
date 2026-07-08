package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// ══════════════════════════════════════════════════════
//  Test Cases — Section 4 : SwiftCargo Co., Ltd.
//  รันด้วย:  mvn test
//            mvn -Dtest=ShipmentTest_Section4 test
// ══════════════════════════════════════════════════════
@DisplayName("Section 4 — SwiftCargo Co., Ltd.")
class ShipmentTest_Section4 {

    @Test
    @DisplayName("SC001: 5.0 กก. STANDARD ต้องได้ 200.00 บาท")
    void sec4_standard_5kg() {
        Shipment s = new Shipment("SC001", 5.0, ShipmentType.STANDARD);
        assertEquals(200.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("SC002: 1.0 กก. EXPRESS ต้องได้ 100.00 บาท")
    void sec4_express_1kg() {
        Shipment s = new Shipment("SC002", 1.0, ShipmentType.EXPRESS);
        assertEquals(100.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("SC003: 4.0 กก. EXPRESS ต้องได้ 400.00 บาท")
    void sec4_express_4kg() {
        Shipment s = new Shipment("SC003", 4.0, ShipmentType.EXPRESS);
        assertEquals(400.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("SC004: 9.0 กก. STANDARD ต้องได้ 360.00 บาท")
    void sec4_standard_9kg() {
        Shipment s = new Shipment("SC004", 9.0, ShipmentType.STANDARD);
        assertEquals(360.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("SC005: 2.5 กก. EXPRESS ต้องได้ 250.00 บาท")
    void sec4_express_2_5kg() {
        Shipment s = new Shipment("SC005", 2.5, ShipmentType.EXPRESS);
        assertEquals(250.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("ยอดรวม SwiftCargo ต้องได้ 1,310.00 บาท")
    void sec4_totalCost() {
        ShippingCompany company = new ShippingCompany("SwiftCargo Co., Ltd.");
        company.addShipment(new Shipment("SC001", 5.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("SC002", 1.0, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("SC003", 4.0, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("SC004", 9.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("SC005", 2.5, ShipmentType.EXPRESS));
        assertEquals(1310.00, company.getTotalCost(), 0.01);
    }

    @Test
    @DisplayName("[EDGE] ไม่มี Shipment ยอดรวมต้องเป็น 0")
    void sec4_emptyCompany() {
        ShippingCompany company = new ShippingCompany("SwiftCargo Co., Ltd.");
        assertEquals(0.00, company.getTotalCost(), 0.01);
    }
}
