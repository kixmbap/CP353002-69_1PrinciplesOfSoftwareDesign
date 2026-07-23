# Assignment: My First REST API — "Coffee Menu Service"

> **รายวิชา:** CP353002 — Principles of Software Design and Development
> **ประเภท:** Mini Project (ทำเดี่ยว หรือกลุ่ม 2 คน)
> **เทคโนโลยี:** Java 17+, Spring Boot 3.x, Maven หรือ Gradle
> **เก็บข้อมูล:** ใน memory (List ธรรมดา) — **ยังไม่ต้องใช้ฐานข้อมูล**
> **สิ่งที่ต้องส่ง:** Source code (สร้าง repo เอง **ไม่ต้อง fork**) + รายงานผลทดสอบสั้น ๆ

---

## 1. ที่มาของโจทย์

โจทย์นี้ให้ฝึก **สร้าง REST API เบื้องต้น** ด้วย Spring Boot ตั้งแต่ศูนย์ เน้นให้เข้าใจ 3 เรื่องหลักของหลักการออกแบบและพัฒนาซอฟต์แวร์:

1. **HTTP methods** (GET, POST, PUT, DELETE) กับความหมายของแต่ละตัว
2. **การแยกชั้น (layered design)** — Controller / Service / Model แยกหน้าที่กันชัดเจน
3. **การรับ–ส่งข้อมูลแบบ JSON** ผ่าน API

ยังไม่ต้องมีฐานข้อมูล ไม่ต้องมี login ไม่ต้องมีหน้าเว็บ — เก็บข้อมูลไว้ใน `List` ใน memory ก็พอ

---

## 2. โจทย์ (Problem Statement)

ให้สร้าง Spring Boot project หนึ่งโปรเจกต์ ทำ REST API ของ **"ร้านกาแฟ"** สำหรับจัดการรายการเมนู (เพิ่ม / ดู / แก้ไข / ลบ)

### 2.1 Model — คลาส `Coffee`
สร้างคลาสข้อมูลเมนูกาแฟ มี field อย่างน้อย:

| Field | ชนิด | ตัวอย่าง |
|---|---|---|
| `id` | `int` หรือ `Long` | 1 |
| `name` | `String` | "Latte" |
| `price` | `double` | 55.0 |

### 2.2 ต้องแยกโค้ดเป็น 3 ชั้น (Layered Design)

| ชั้น | หน้าที่ | ตัวอย่างชื่อคลาส |
|---|---|---|
| **Model** | เก็บโครงสร้างข้อมูล | `Coffee` |
| **Service** | เก็บ logic + `List<Coffee>` ใน memory | `CoffeeService` |
| **Controller** | รับ HTTP request แล้วเรียก Service | `CoffeeController` |

> Controller **ห้าม** เก็บข้อมูลเอง ต้องเรียกผ่าน Service เท่านั้น (นี่คือหัวใจของโจทย์: แยกหน้าที่ให้ถูก)

---

## 3. Endpoints ที่ต้องมี (Required API)

| # | Method | Path | ทำอะไร |
|---|---|---|---|
| 1 | `GET` | `/coffees` | ดูเมนูทั้งหมด |
| 2 | `GET` | `/coffees/{id}` | ดูเมนู 1 รายการตาม id |
| 3 | `POST` | `/coffees` | เพิ่มเมนูใหม่ (ส่งข้อมูลมาเป็น JSON) |
| 4 | `PUT` | `/coffees/{id}` | แก้ไขเมนูเดิมตาม id |
| 5 | `DELETE` | `/coffees/{id}` | ลบเมนูตาม id |

**เมื่อเริ่มรันแอป** ให้ใส่ข้อมูลตัวอย่างไว้ล่วงหน้าอย่างน้อย 2 รายการ เพื่อให้ทดสอบ `GET` ได้ทันที

---

## 4. Expected Result (ผลลัพธ์ที่ต้องได้)

ทดสอบด้วย **Postman** หรือ **curl** ก็ได้ แล้วแคปผลตามนี้

### 4.1 GET ทั้งหมด
```
GET /coffees   → 200 OK
[
  { "id": 1, "name": "Espresso", "price": 45.0 },
  { "id": 2, "name": "Latte",    "price": 55.0 }
]
```

### 4.2 GET ตาม id
```
GET /coffees/1   → 200 OK
{ "id": 1, "name": "Espresso", "price": 45.0 }
```

### 4.3 POST เพิ่มเมนูใหม่
```
POST /coffees
Body (JSON):
{ "name": "Cappuccino", "price": 60.0 }

→ 201 Created (หรือ 200 OK)
{ "id": 3, "name": "Cappuccino", "price": 60.0 }
```
✅ หลังจากนี้ `GET /coffees` ต้องมี 3 รายการ

### 4.4 PUT แก้ไข
```
PUT /coffees/2
Body (JSON):
{ "name": "Latte", "price": 50.0 }

→ 200 OK
{ "id": 2, "name": "Latte", "price": 50.0 }   ← ราคาเปลี่ยนแล้ว
```

### 4.5 DELETE ลบ
```
DELETE /coffees/3   → 200 OK (หรือ 204 No Content)
```
✅ หลังจากนี้ `GET /coffees` ต้องเหลือ 2 รายการ

---

## 5. Discussion (ตอบในรายงาน)

ตอบสั้น ๆ ข้อละ 2–4 บรรทัด:

1. HTTP method แต่ละตัว (GET/POST/PUT/DELETE) ต่างกันอย่างไร ยกตัวอย่างจากโปรเจกต์ตัวเอง
2. ทำไมต้องแยก Controller กับ Service ออกจากกัน มีข้อดีอย่างไรถ้าโปรแกรมโตขึ้น
3. ข้อมูลที่เก็บไว้ใน `List` ใน memory หายไปตอนไหน และถ้าอยากให้ไม่หายควรทำอย่างไร (ตอบเป็นแนวคิดพอ)
4. `@RestController`, `@GetMapping`, `@PostMapping`, `@PathVariable`, `@RequestBody` แต่ละตัวทำหน้าที่อะไร

---

## 6. เกณฑ์การให้คะแนน (Rubric — 100 คะแนน)

| หัวข้อ | คะแนน | เกณฑ์ |
|---|---|---|
| แยกชั้น Model / Service / Controller ถูกต้อง | 20 | Controller ไม่เก็บข้อมูลเอง |
| Endpoint ครบ 5 ตัว ทำงานได้ | 40 | ข้อละ 8 คะแนน |
| ผลลัพธ์ตรงกับ Expected Result §4 | 20 | มีหลักฐาน (แคป Postman/curl) |
| รายงาน Discussion §5 | 10 | ตอบครบ เข้าใจจริง |
| Code quality (ตั้งชื่อดี, อ่านง่าย, เป็นระเบียบ) | 10 | สะอาด ไม่รก |

**โบนัส (+10):** ทำอย่างใดอย่างหนึ่ง — คืน `404 Not Found` เมื่อหา id ไม่เจอ / เพิ่ม endpoint ค้นหาตามชื่อ `GET /coffees/search?name=...` / เขียน README มีตัวอย่าง curl ครบทุก endpoint

---

## 7. สิ่งที่ต้องส่ง (Submission)

1. **Source code** — push ขึ้น GitHub repo ของตัวเอง (สร้างใหม่ ไม่ต้อง fork) ส่งลิงก์
2. **README.md** — วิธีรัน (`mvn spring-boot:run` หรือเทียบเท่า) + ตัวอย่างการเรียก API
3. **รายงานผลทดสอบ** (PDF หรือ Markdown) — แคปผล 5 endpoint + คำตอบ Discussion 5


## 8. คำแนะนำสำหรับผู้เริ่มต้น (Hints)

- สร้างโปรเจกต์เร็ว ๆ ได้ที่ **[start.spring.io](https://start.spring.io)** เลือก dependency: **Spring Web** อย่างเดียวก็พอ
- ตัวอย่างการยิงด้วย curl:
  ```bash
  curl http://localhost:8080/coffees
  curl -X POST http://localhost:8080/coffees \
       -H "Content-Type: application/json" \
       -d '{"name":"Mocha","price":65}'
  ```
- ถ้า POST แล้วขึ้น error 415 → ลืมใส่ header `Content-Type: application/json`
- ถ้า `id` ซ้ำกัน → ตอนเพิ่มเมนูใหม่ต้อง gen id เอง (เช่น นับต่อจากตัวสุดท้าย)
- ยังไม่ต้องกังวลเรื่องฐานข้อมูล / security / หน้าเว็บ — โฟกัสที่ API ทำงานถูกและโค้ดแยกชั้นสะอาด

---

*หมายเหตุสำหรับผู้สอน: ไฟล์นี้เป็นตัวโจทย์ + expected result ไม่มีเฉลยโค้ดแนบมา นักศึกษาต้อง implement เอง ปรับ entity (เช่นเปลี่ยนจากกาแฟเป็นหนังสือ/สินค้า), จำนวน endpoint และคะแนนได้ตามความเหมาะสม*
