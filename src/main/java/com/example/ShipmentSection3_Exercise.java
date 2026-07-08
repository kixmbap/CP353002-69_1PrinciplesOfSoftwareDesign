import java.util.ArrayList;
import java.util.List;

// ╔══════════════════════════════════════════════════════════╗
//  SECTION 3 — แบบฝึกหัด (Exercise)
//  ชื่อนักศึกษา : ___________________________
//  รหัสนักศึกษา : ___________________________
// ╚══════════════════════════════════════════════════════════╝
//
//  โจทย์:
//    บริษัท RocketShip Thailand มีรายการ Shipment หลายรายการ
//    ให้คำนวณค่าขนส่งตามน้ำหนักและประเภท แล้วแสดงยอดรวม
//
//  กฎการคำนวณ:
//    ประเภท STANDARD  →   40 บาท / กิโลกรัม
//    ประเภท EXPRESS   →  100 บาท / กิโลกรัม
//
//  คำสั่ง:
//    หา Bug และเติม code ในทุกจุดที่มี 👉 TODO
//    แล้วรันให้ได้ผลลัพธ์ตาม ExpectedOutput_Section3.md
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

    // 👉 TODO A : if-else ใน calculateCost() สลับกัน
    //             ตอนนี้ STANDARD ใช้ EXPRESS_RATE และ EXPRESS ใช้ STANDARD_RATE
    //             แก้ให้ถูกต้อง (แก้แค่ if condition อย่างเดียว)
    public double calculateCost() {
        final double STANDARD_RATE =  40.0;
        final double EXPRESS_RATE  = 100.0;
        if (type == ShipmentType.EXPRESS) {          // ← ผิด ควรเช็ค STANDARD
            return weightKg * STANDARD_RATE;
        } else {
            return weightKg * EXPRESS_RATE;
        }
    }

    // 👉 TODO B : toString() ยังไม่สมบูรณ์
    //             ให้แสดงในรูปแบบนี้ (ดูตัวอย่างใน ExpectedOutput_Section3.md):
    //             [RS001]  2.00 กก. | STANDARD |     80.00 บาท
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

    // 👉 TODO C : getTotalCost() วนลูปผิด — ใช้ index ผิด
    //             ตอนนี้ดึงแค่ shipments.get(0) ทุกรอบ
    //             แก้ให้ดึง shipments.get(i) ให้ถูกต้อง
    public double getTotalCost() {
        double total = 0;
        for (int i = 0; i < shipments.size(); i++) {
            total += shipments.get(0).calculateCost();  // ← ผิด ควรเป็น get(i)
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
public class ShipmentSection3_Exercise {
    public static void main(String[] args) {

        ShippingCompany company = new ShippingCompany("RocketShip Thailand");

        // (trackingNumber, weightKg, type)
        company.addShipment(new Shipment("RS001",  2.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("RS002",  3.5,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("RS003",  7.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("RS004",  0.5,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("RS005", 12.0,  ShipmentType.STANDARD));

        company.printSummary();
    }
}
