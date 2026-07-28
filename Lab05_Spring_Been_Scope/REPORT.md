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
<img width="793" height="282" alt="image" src="https://github.com/user-attachments/assets/21884bd4-afed-42f2-adaf-982729e62e2a" />

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
<img width="657" height="245" alt="image" src="https://github.com/user-attachments/assets/55ff4b72-2617-425a-9d31-2676fa9434f2" />

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
<img width="620" height="342" alt="image" src="https://github.com/user-attachments/assets/43529f89-9e70-4ab3-a464-f0e09b5721e4" />
<img width="1023" height="258" alt="image" src="https://github.com/user-attachments/assets/1cc8615e-6046-4b0b-af0e-107446c0b2f8" />


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
<img width="1100" height="467" alt="image" src="https://github.com/user-attachments/assets/9cad41db-8be8-4152-8e67-c6aaa0a65fd4" />

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
<img width="1093" height="417" alt="image" src="https://github.com/user-attachments/assets/83b16bd7-8454-491a-87b6-f8cfba255a73" />
<img width="1106" height="227" alt="image" src="https://github.com/user-attachments/assets/582f52d6-985a-4982-9ea2-bc4d181be5ae" />

### 2.6 กรณีไม่พบข้อมูล (โบนัส)
- วิธีทดสอบ:
  - curl http://localhost:8080/coffees/999
- ผลที่คาดหวัง:
  - 404 Not Found
- ผลที่ได้จริง:
  - ผ่าน
- หมายเหตุ:
  - รองรับการคืนค่า 404 แล้ว
<img width="1087" height="226" alt="image" src="https://github.com/user-attachments/assets/60c7d33f-7cb4-4387-8852-4fb9838000d2" />

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
