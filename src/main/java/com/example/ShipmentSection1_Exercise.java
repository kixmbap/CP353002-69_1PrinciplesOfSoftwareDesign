package com.example;

import java.util.ArrayList;
import java.util.List;

// ╔══════════════════════════════════════════════════════════╗
//  SECTION 1 — แบบฝึกหัด (Exercise)
//  ชื่อนักศึกษา : ___________________________
//  รหัสนักศึกษา : ___________________________
// ╚══════════════════════════════════════════════════════════╝
//
//  โจทย์:
//    บริษัท SpeedEx Logistics มีรายการ Shipment หลายรายการ
//    ให้คำนวณค่าขนส่งตามน้ำหนักและประเภท แล้วแสดงยอดรวม
//
//  กฎการคำนวณ:
//    ประเภท STANDARD  →   40 บาท / กิโลกรัม
//    ประเภท EXPRESS   →  100 บาท / กิโลกรัม
//
//  คำสั่ง:
//    หา Bug และเติม code ในทุกจุดที่มี 👉 TODO
//    แล้วรันให้ได้ผลลัพธ์ตาม ExpectedOutput_Section1.md
// ══════════════════════════════════════════════════════════

// ──────────────────────────────────────────────────────────
//  PART A : Enum ประเภทการขนส่ง
// ──────────────────────────────────────────────────────────
// 👉 TODO A : enum นี้มีแค่ EXPRESS
//             เพิ่ม STANDARD ให้ครบด้วย
enum ShipmentType {
    STANDARD,
    EXPRESS
}

// ──────────────────────────────────────────────────────────
//  PART B : Class Shipment — ข้อมูลพัสดุแต่ละรายการ
// ──────────────────────────────────────────────────────────
class Shipment {

    private String       trackingNumber;
    private double       weightKg;
    private ShipmentType type;

    // ลำดับ parameter ถูกต้องเป็น (trackingNumber, weightKg, type)
    public Shipment(String trackingNumber, double weightKg, ShipmentType type) {
        this.trackingNumber = trackingNumber;
        this.weightKg       = weightKg;
        this.type           = type;
    }

    public String       getTrackingNumber() { return trackingNumber; }
    public double       getWeightKg()       { return weightKg;       }
    public ShipmentType getType()           { return type;           }

    // 👉 TODO C : ค่าอัตราค่าขนส่งสลับกัน!
    //             STANDARD_RATE ต้องเป็น  40.0
    //             EXPRESS_RATE  ต้องเป็น 100.0
    //             แก้ให้ถูกต้อง
    public double calculateCost() {
        final double STANDARD_RATE = 40.0;
        final double EXPRESS_RATE  = 100.0;
        if (type == ShipmentType.STANDARD) {
            return weightKg * STANDARD_RATE;
        } else {
            return weightKg * EXPRESS_RATE;
        }
    }

    // 👉 TODO D : toString() ยังไม่สมบูรณ์
    //             ให้แสดงในรูปแบบนี้ (ดูตัวอย่างใน ExpectedOutput_Section1.md):
    //             [TH001]  3.00 กก. | STANDARD |    120.00 บาท
    //             แนะนำ: ใช้ String.format() และเรียก calculateCost()
    @Override
    public String toString() {
        return String.format("[%s] %5.2f กก. | %-8s | %10.2f บาท",
                             trackingNumber, weightKg, type, calculateCost());
    }
}

// ──────────────────────────────────────────────────────────
//  PART C : Class ShippingCompany — บริษัทขนส่ง
// ──────────────────────────────────────────────────────────
class ShippingCompany {

    private String         name;
    private List<Shipment> shipments;

    // 👉 TODO E : ลืม initialize shipments
    //             ถ้ารันตอนนี้จะ crash ด้วย NullPointerException
    //             เพิ่ม  shipments = new ArrayList<>();  ใน constructor
    public ShippingCompany(String name) {
        this.name = name;
        // เพิ่มบรรทัด initialize ตรงนี้
        this.shipments = new ArrayList<>();
    }

    public void addShipment(Shipment s) {
        shipments.add(s);
    }

    // 👉 TODO F : getTotalCost() ยังไม่ได้วนลูปจริง
    //             ให้รวม calculateCost() ของทุก Shipment ใน list
    public double getTotalCost() {
        double total = 0;
        for (Shipment s : shipments) {
            total += s.calculateCost();
        }
        return total;
    }

    // 👉 TODO G : printSummary() ยังขาด 2 ส่วน
    //             1) loop แสดงรายการแต่ละ shipment
    //             2) บรรทัดแสดงยอดรวม
    //             เติมทั้งสองส่วนนั้น
    public void printSummary() {
        System.out.println("========================================");
        System.out.printf ("  บริษัท        : %s%n",   name);
        System.out.printf ("  จำนวน Shipment : %d รายการ%n", shipments.size());
        System.out.println("========================================");

        // 1) วนลูปแสดงแต่ละ shipment
        for (Shipment s : shipments) {
            System.out.println(s.toString());
        }

        System.out.println("----------------------------------------");
        // 2) แสดงยอดรวม
        System.out.printf("  ยอดรวมทั้งหมด : %s บาท%n", String.format("%,.2f", getTotalCost()));
        System.out.println("========================================");
    }
}

// ──────────────────────────────────────────────────────────
//  PART D : Main
// ──────────────────────────────────────────────────────────
public class ShipmentSection1_Exercise {
    public static void main(String[] args) {

        ShippingCompany company = new ShippingCompany("SpeedEx Logistics");

        // (trackingNumber, weightKg, type)
        company.addShipment(new Shipment("TH001",  3.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("TH002",  1.5,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("TH003",  5.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("TH004",  2.0,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("TH005", 10.0,  ShipmentType.STANDARD));

        company.printSummary();
    }
}
