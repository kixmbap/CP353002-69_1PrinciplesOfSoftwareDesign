package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// ══════════════════════════════════════════════════════
//  Test Cases — Section 1 : SpeedEx Logistics
//  รันด้วย:  mvn test
//            mvn -Dtest=ShipmentTest_Section1 test
// ══════════════════════════════════════════════════════
@DisplayName("Section 1 — SpeedEx Logistics")
class ShipmentTest_Section1 {

    @Test
    @DisplayName("TH001: 3.0 กก. STANDARD ต้องได้ 120.00 บาท")
    void sec1_standard_3kg() {
        Shipment s = new Shipment("TH001", 3.0, ShipmentType.STANDARD);
        assertEquals(120.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("TH002: 1.5 กก. EXPRESS ต้องได้ 150.00 บาท")
    void sec1_express_1_5kg() {
        Shipment s = new Shipment("TH002", 1.5, ShipmentType.EXPRESS);
        assertEquals(150.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("TH003: 5.0 กก. STANDARD ต้องได้ 200.00 บาท")
    void sec1_standard_5kg() {
        Shipment s = new Shipment("TH003", 5.0, ShipmentType.STANDARD);
        assertEquals(200.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("TH004: 2.0 กก. EXPRESS ต้องได้ 200.00 บาท")
    void sec1_express_2kg() {
        Shipment s = new Shipment("TH004", 2.0, ShipmentType.EXPRESS);
        assertEquals(200.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("TH005: 10.0 กก. STANDARD ต้องได้ 400.00 บาท")
    void sec1_standard_10kg() {
        Shipment s = new Shipment("TH005", 10.0, ShipmentType.STANDARD);
        assertEquals(400.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("ยอดรวม SpeedEx ต้องได้ 1,070.00 บาท")
    void sec1_totalCost() {
        ShippingCompany company = new ShippingCompany("SpeedEx Logistics");
        company.addShipment(new Shipment("TH001",  3.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("TH002",  1.5, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("TH003",  5.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("TH004",  2.0, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("TH005", 10.0, ShipmentType.STANDARD));
        assertEquals(1070.00, company.getTotalCost(), 0.01);
    }

    @Test
    @DisplayName("[EDGE] น้ำหนัก 0 กก. ต้องได้ 0.00 บาท")
    void sec1_zeroWeight() {
        Shipment s = new Shipment("TH000", 0.0, ShipmentType.EXPRESS);
        assertEquals(0.00, s.calculateCost(), 0.01);
    }
}
