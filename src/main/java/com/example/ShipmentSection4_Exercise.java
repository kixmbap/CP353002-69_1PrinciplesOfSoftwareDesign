import java.util.ArrayList;
import java.util.List;

// ╔══════════════════════════════════════════════════════════╗
//  SECTION 4 — แบบฝึกหัด (Exercise)
//  ชื่อนักศึกษา : ___________________________
//  รหัสนักศึกษา : ___________________________
// ╚══════════════════════════════════════════════════════════╝
//
//  โจทย์:
//    บริษัท SwiftCargo Co., Ltd. มีรายการ Shipment หลายรายการ
//    ให้คำนวณค่าขนส่งตามน้ำหนักและประเภท แล้วแสดงยอดรวม
//
//  กฎการคำนวณ:
//    ประเภท STANDARD  →   40 บาท / กิโลกรัม
//    ประเภท EXPRESS   →  100 บาท / กิโลกรัม
//
//  คำสั่ง:
//    หา Bug และเติม code ในทุกจุดที่มี 👉 TODO
//    แล้วรันให้ได้ผลลัพธ์ตาม ExpectedOutput_Section4.md
// ══════════════════════════════════════════════════════════

// ──────────────────────────────────────────────────────────
//  PART A : Enum ประเภทการขนส่ง
// ──────────────────────────────────────────────────────────
// ✅ ส่วนนี้ถูกต้องแล้ว ไม่ต้องแก้
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

    // ✅ Constructor ถูกต้องแล้ว ไม่ต้องแก้
    public Shipment(String trackingNumber, double weightKg, ShipmentType type) {
        this.trackingNumber = trackingNumber;
        this.weightKg       = weightKg;
        this.type           = type;
    }

    public String       getTrackingNumber() { return trackingNumber; }
    public double       getWeightKg()       { return weightKg;       }
    public ShipmentType getType()           { return type;           }

    // 👉 TODO A : calculateCost() คืนค่า 0 เสมอ
    //             เพราะ return อยู่นอก if-else และ local variables ไม่ถูกใช้
    //             แก้ให้ return ค่าที่คำนวณได้จริง
    public double calculateCost() {
        final double STANDARD_RATE =  40.0;
        final double EXPRESS_RATE  = 100.0;
        double cost = 0;
        if (type == ShipmentType.STANDARD) {
            cost = weightKg * STANDARD_RATE;
        } else {
            cost = weightKg * EXPRESS_RATE;
        }
        return 0;  // ← ผิด ควร return cost
    }

    // 👉 TODO B : toString() ยังไม่สมบูรณ์
    //             ให้แสดงในรูปแบบนี้ (ดูตัวอย่างใน ExpectedOutput_Section4.md):
    //             [SC001]  5.00 กก. | STANDARD |    200.00 บาท
    //             แนะนำ: ใช้ String.format() และเรียก calculateCost()
    @Override
    public String toString() {
        return "[" + trackingNumber + "] ???";  // ← เติมให้ครบ
    }
}

// ──────────────────────────────────────────────────────────
//  PART C : Class ShippingCompany — บริษัทขนส่ง
// ──────────────────────────────────────────────────────────
class ShippingCompany {

    private String         name;
    private List<Shipment> shipments;

    // ✅ initialize ถูกต้องแล้ว ไม่ต้องแก้
    public ShippingCompany(String name) {
        this.name      = name;
        this.shipments = new ArrayList<>();
    }

    public void addShipment(Shipment s) {
        shipments.add(s);
    }

    // 👉 TODO C : getTotalCost() วนลูปผิด — นับ i เกิน 1 รายการ
    //             loop condition ผิด ทำให้วนเฉพาะรายการแรกอย่างเดียว
    //             แก้ loop condition ให้ถูกต้อง
    public double getTotalCost() {
        double total = 0;
        for (int i = 0; i < 1; i++) {          // ← ผิด ควรเป็น i < shipments.size()
            total += shipments.get(i).calculateCost();
        }
        return total;
    }

    // 👉 TODO D : printSummary() ยังขาด 2 ส่วน
    //             1) loop แสดงรายการแต่ละ shipment
    //             2) บรรทัดแสดงยอดรวม
    //             เติมทั้งสองส่วนนั้น
    public void printSummary() {
        System.out.println("========================================");
        System.out.printf ("  บริษัท        : %s%n",   name);
        System.out.printf ("  จำนวน Shipment : %d รายการ%n", shipments.size());
        System.out.println("========================================");

        // 1) วนลูปแสดงแต่ละ shipment ตรงนี้

        System.out.println("----------------------------------------");
        // 2) แสดงยอดรวมตรงนี้
    }
}

// ──────────────────────────────────────────────────────────
//  PART D : Main
// ──────────────────────────────────────────────────────────
public class ShipmentSection4_Exercise {
    public static void main(String[] args) {

        ShippingCompany company = new ShippingCompany("SwiftCargo Co., Ltd.");

        // (trackingNumber, weightKg, type)
        company.addShipment(new Shipment("SC001",  5.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("SC002",  1.0,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("SC003",  4.0,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("SC004",  9.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("SC005",  2.5,  ShipmentType.EXPRESS));

        company.printSummary();
    }
}
