import java.util.ArrayList;
import java.util.List;

// ╔══════════════════════════════════════════════════════════╗
//  SECTION 2 — แบบฝึกหัด (Exercise)
//  ชื่อนักศึกษา : ___________________________
//  รหัสนักศึกษา : ___________________________
// ╚══════════════════════════════════════════════════════════╝
//
//  โจทย์:
//    บริษัท FlashMove Express มีรายการ Shipment หลายรายการ
//    ให้คำนวณค่าขนส่งตามน้ำหนักและประเภท แล้วแสดงยอดรวม
//
//  กฎการคำนวณ:
//    ประเภท STANDARD  →   40 บาท / กิโลกรัม
//    ประเภท EXPRESS   →  100 บาท / กิโลกรัม
//
//  คำสั่ง:
//    หา Bug และเติม code ในทุกจุดที่มี 👉 TODO
//    แล้วรันให้ได้ผลลัพธ์ตาม ExpectedOutput_Section2.md
// ══════════════════════════════════════════════════════════

// ──────────────────────────────────────────────────────────
//  PART A : Enum ประเภทการขนส่ง
// ──────────────────────────────────────────────────────────
// 👉 TODO A : enum นี้มีแค่ STANDARD
//             เพิ่ม EXPRESS ให้ครบด้วย
enum ShipmentType {
    STANDARD
    // เพิ่ม EXPRESS ตรงนี้
}

// ──────────────────────────────────────────────────────────
//  PART B : Class Shipment — ข้อมูลพัสดุแต่ละรายการ
// ──────────────────────────────────────────────────────────
class Shipment {

    private String       trackingNumber;
    private double       weightKg;
    private ShipmentType type;

    // 👉 TODO B : assignment ใน Constructor ผิดทั้งคู่
    //             weightKg ถูก assign ให้ type
    //             type ถูก assign ให้ weightKg
    //             แก้ให้ถูกต้อง
    public Shipment(String trackingNumber, double weightKg, ShipmentType type) {
        this.trackingNumber = trackingNumber;
        this.weightKg       = type;       // ← ผิด ควรเป็น weightKg
        this.type           = weightKg;   // ← ผิด ควรเป็น type
    }

    public String       getTrackingNumber() { return trackingNumber; }
    public double       getWeightKg()       { return weightKg;       }
    public ShipmentType getType()           { return type;           }

    // 👉 TODO C : สูตรคำนวณผิด — คูณ weightKg ด้วย 1 เสมอ
    //             แก้ให้ใช้ STANDARD_RATE และ EXPRESS_RATE ที่ถูกต้อง
    //             STANDARD_RATE = 40.0 , EXPRESS_RATE = 100.0
    public double calculateCost() {
        final double STANDARD_RATE = 1.0;   // ← ผิด ควรเป็น 40.0
        final double EXPRESS_RATE  = 1.0;   // ← ผิด ควรเป็น 100.0
        if (type == ShipmentType.STANDARD) {
            return weightKg * STANDARD_RATE;
        } else {
            return weightKg * EXPRESS_RATE;
        }
    }

    // 👉 TODO D : toString() ยังไม่สมบูรณ์
    //             ให้แสดงในรูปแบบนี้ (ดูตัวอย่างใน ExpectedOutput_Section2.md):
    //             [FM001]  4.00 กก. | STANDARD |    160.00 บาท
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

    // 👉 TODO E : ลืม initialize shipments
    //             ถ้ารันตอนนี้จะ crash ด้วย NullPointerException
    //             เพิ่ม  shipments = new ArrayList<>();  ใน constructor
    public ShippingCompany(String name) {
        this.name = name;
        // เพิ่มบรรทัด initialize ตรงนี้
    }

    public void addShipment(Shipment s) {
        shipments.add(s);
    }

    // 👉 TODO F : getTotalCost() คืนค่า 0 เสมอ เพราะยังไม่ได้วนลูปจริง
    //             ให้รวม calculateCost() ของทุก Shipment ใน list
    public double getTotalCost() {
        double total = 0;
        // วนลูปรวม cost ของแต่ละ shipment ตรงนี้
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

        // 1) วนลูปแสดงแต่ละ shipment ตรงนี้

        System.out.println("----------------------------------------");
        // 2) แสดงยอดรวมตรงนี้
    }
}

// ──────────────────────────────────────────────────────────
//  PART D : Main
// ──────────────────────────────────────────────────────────
public class ShipmentSection2_Exercise {
    public static void main(String[] args) {

        ShippingCompany company = new ShippingCompany("FlashMove Express");

        // (trackingNumber, weightKg, type)
        company.addShipment(new Shipment("FM001",  4.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("FM002",  2.5,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("FM003",  6.0,  ShipmentType.STANDARD));
        company.addShipment(new Shipment("FM004",  1.0,  ShipmentType.EXPRESS));
        company.addShipment(new Shipment("FM005",  8.0,  ShipmentType.STANDARD));

        company.printSummary();
    }
}
