package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// ══════════════════════════════════════════════════════
//  Test Cases — Section 3 : RocketShip Thailand
//  รันด้วย:  mvn test
//            mvn -Dtest=ShipmentTest_Section3 test
// ══════════════════════════════════════════════════════
@DisplayName("Section 3 — RocketShip Thailand")
class ShipmentTest_Section3 {

    @Test
    @DisplayName("RS001: 2.0 กก. STANDARD ต้องได้ 80.00 บาท")
    void sec3_standard_2kg() {
        Shipment s = new Shipment("RS001", 2.0, ShipmentType.STANDARD);
        assertEquals(80.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("RS002: 3.5 กก. EXPRESS ต้องได้ 350.00 บาท")
    void sec3_express_3_5kg() {
        Shipment s = new Shipment("RS002", 3.5, ShipmentType.EXPRESS);
        assertEquals(350.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("RS003: 7.0 กก. STANDARD ต้องได้ 280.00 บาท")
    void sec3_standard_7kg() {
        Shipment s = new Shipment("RS003", 7.0, ShipmentType.STANDARD);
        assertEquals(280.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("RS004: 0.5 กก. EXPRESS ต้องได้ 50.00 บาท")
    void sec3_express_0_5kg() {
        Shipment s = new Shipment("RS004", 0.5, ShipmentType.EXPRESS);
        assertEquals(50.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("RS005: 12.0 กก. STANDARD ต้องได้ 480.00 บาท")
    void sec3_standard_12kg() {
        Shipment s = new Shipment("RS005", 12.0, ShipmentType.STANDARD);
        assertEquals(480.00, s.calculateCost(), 0.01);
    }

    @Test
    @DisplayName("ยอดรวม RocketShip ต้องได้ 1,240.00 บาท")
    void sec3_totalCost() {
        ShippingCompany company = new ShippingCompany("RocketShip Thailand");
        company.addShipment(new Shipment("RS001",  2.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("RS002",  3.5, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("RS003",  7.0, ShipmentType.STANDARD));
        company.addShipment(new Shipment("RS004",  0.5, ShipmentType.EXPRESS));
        company.addShipment(new Shipment("RS005", 12.0, ShipmentType.STANDARD));
        assertEquals(1240.00, company.getTotalCost(), 0.01);
    }

    @Test
    @DisplayName("[EDGE] น้ำหนัก 0 กก. ต้องได้ 0.00 บาท")
    void sec3_zeroWeight() {
        Shipment s = new Shipment("RS000", 0.0, ShipmentType.STANDARD);
        assertEquals(0.00, s.calculateCost(), 0.01);
    }
}
