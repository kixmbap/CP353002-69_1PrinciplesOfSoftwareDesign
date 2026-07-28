# รายงานผลทดสอบ Coffee Menu Service

รายวิชา: CP353002 Principles of Software Design and Development

## 1) ข้อมูลโปรเจกต์

- ชื่อโปรเจกต์: Coffee Menu Service
- เทคโนโลยี: Java 17, Spring Boot 3.x, Maven
- รูปแบบจัดโครงสร้าง: Layered Design (Controller / Service / Model)
- การจัดเก็บข้อมูล: List ในหน่วยความจำ (In-Memory)

## 2) ผลการทดสอบ API

### 2.1 GET /coffees
- วิธีทดสอบ:
  - curl http://localhost:8080/coffees
- ผลที่คาดหวัง:
  - 200 OK
  - ได้รายการเมนูเริ่มต้นอย่างน้อย 2 รายการ
- ผลที่ได้จริง:
  - ผ่าน
- หลักฐาน:
  - แนบภาพหน้าจอผลลัพธ์ใน Postman หรือ Terminal

### 2.2 GET /coffees/{id}
- วิธีทดสอบ:
  - curl http://localhost:8080/coffees/1
- ผลที่คาดหวัง:
  - 200 OK
  - ได้ข้อมูลเมนูรหัส 1
- ผลที่ได้จริง:
  - ผ่าน
- หลักฐาน:
  - แนบภาพหน้าจอผลลัพธ์

### 2.3 POST /coffees
- วิธีทดสอบ:
  - curl -X POST http://localhost:8080/coffees -H "Content-Type: application/json" -d '{"name":"Cappuccino","price":60.0}'
- ผลที่คาดหวัง:
  - 201 Created
  - ได้ข้อมูลเมนูใหม่พร้อม id ที่ระบบสร้างให้
- ผลที่ได้จริง:
  - ผ่าน
- หลักฐาน:
  - แนบภาพหน้าจอผลลัพธ์

### 2.4 PUT /coffees/{id}
- วิธีทดสอบ:
  - curl -X PUT http://localhost:8080/coffees/2 -H "Content-Type: application/json" -d '{"name":"Latte","price":50.0}'
- ผลที่คาดหวัง:
  - 200 OK
  - ราคาเมนูถูกแก้ไขตามข้อมูลใหม่
- ผลที่ได้จริง:
  - ผ่าน
- หลักฐาน:
  - แนบภาพหน้าจอผลลัพธ์

### 2.5 DELETE /coffees/{id}
- วิธีทดสอบ:
  - curl -X DELETE http://localhost:8080/coffees/3
- ผลที่คาดหวัง:
  - 204 No Content
  - เมนูที่ลบไม่ปรากฏในผลลัพธ์ GET ทั้งหมด
- ผลที่ได้จริง:
  - ผ่าน
- หลักฐาน:
  - แนบภาพหน้าจอผลลัพธ์

### 2.6 กรณีไม่พบข้อมูล (โบนัส)
- วิธีทดสอบ:
  - curl http://localhost:8080/coffees/999
- ผลที่คาดหวัง:
  - 404 Not Found
- ผลที่ได้จริง:
  - ผ่าน
- หมายเหตุ:
  - รองรับการคืนค่า 404 แล้ว

## 3) Discussion

### 3.1 ความแตกต่างของ HTTP Methods (GET, POST, PUT, DELETE)
GET ใช้สำหรับอ่านข้อมูล เช่น ดูรายการกาแฟทั้งหมดหรือดูตามรหัส
POST ใช้สร้างข้อมูลใหม่ เช่น เพิ่มเมนูใหม่เข้าไปในระบบ
PUT ใช้แก้ไขข้อมูลเดิมตามรหัสที่ระบุ
DELETE ใช้ลบข้อมูลตามรหัสที่ต้องการ

### 3.2 เหตุผลที่แยก Controller กับ Service
Controller ควรรับผิดชอบการรับส่ง HTTP request/response เท่านั้น
Service รับผิดชอบ business logic และการจัดการข้อมูล
เมื่อโปรแกรมขยายใหญ่ขึ้น การแยกชั้นช่วยให้แก้ไขง่าย ทดสอบง่าย และลดการผูกกันของโค้ด

### 3.3 ข้อมูลใน List หายตอนไหน และถ้าจะไม่หายทำอย่างไร
ข้อมูลแบบ in-memory จะหายทันทีเมื่อแอปหยุดทำงานหรือรีสตาร์ต
ถ้าต้องการเก็บข้อมูลถาวร ควรใช้ฐานข้อมูล เช่น MySQL หรือ PostgreSQL และเชื่อมผ่าน JPA/Hibernate

### 3.4 หน้าที่ของ Annotation หลัก
@RestController: กำหนดว่าคลาสนี้เป็น REST controller และตอบกลับเป็น JSON
@GetMapping: จับคู่ HTTP GET กับเมธอด
@PostMapping: จับคู่ HTTP POST กับเมธอด
@PathVariable: รับค่าพารามิเตอร์จาก path เช่น /coffees/{id}
@RequestBody: รับข้อมูล JSON จาก body ของ request มาเป็นออบเจ็กต์ Java
