# คอร์สเรียน: Transaction ใน Spring Boot ฉบับเข้าใจง่ายที่สุด
### (สำหรับคนที่ไม่มีพื้นฐานมาก่อนเลย)

---

## บทนำ: ก่อนเริ่ม ต้องเข้าใจอะไรก่อนบ้าง

ก่อนจะไปถึงคำว่า "Transaction" เราต้องปูพื้นฐานกันนิดหน่อยก่อน เพราะถ้าไม่เข้าใจพื้นฐานพวกนี้ อ่านต่อไปจะงงครับ

**Database (ดาต้าเบส / ฐานข้อมูล)** คือที่เก็บข้อมูลของโปรแกรม เช่น ถ้าคุณทำแอปขายของ ข้อมูลลูกค้า ข้อมูลสินค้า ข้อมูลออเดอร์ ทั้งหมดจะถูกเก็บไว้ใน database เหมือนเป็น "ตู้เก็บเอกสารดิจิทัล"

**Spring Boot** คือ เฟรมเวิร์ก (framework = ชุดเครื่องมือสำเร็จรูปที่ช่วยให้เขียนโปรแกรมได้เร็วขึ้น ไม่ต้องเขียนทุกอย่างจากศูนย์) สำหรับเขียนโปรแกรมด้วยภาษา Java โดยเฉพาะพวกระบบหลังบ้าน (backend = ส่วนที่ทำงานเบื้องหลัง ไม่ใช่หน้าจอที่ผู้ใช้เห็น) ของเว็บไซต์หรือแอป

**Annotation (แอนโนเทชัน)** คือ "ป้ายกำกับ" ที่เราติดไว้บนโค้ด เพื่อบอก Spring ว่า "โค้ดตรงนี้ให้ทำงานพิเศษแบบนี้นะ" มันจะขึ้นต้นด้วยเครื่องหมาย `@` เช่น `@Transactional` คือป้ายกำกับที่บอกว่า "method (เมธอด = ฟังก์ชันหรือชุดคำสั่งหนึ่งก้อนในโค้ด) นี้ให้จัดการแบบ transaction นะ"

พร้อมแล้ว มาเริ่มกันเลย

---

## บทที่ 1: Transaction คืออะไร? (อธิบายแบบไม่มีศัพท์เทคนิค)

ลองนึกภาพสถานการณ์นี้ครับ:

> คุณโอนเงินจากบัญชี A ไปบัญชี B จำนวน 1,000 บาท

การโอนเงินจริงๆ แล้วประกอบด้วย 2 ขั้นตอน:
1. **หักเงิน** ออกจากบัญชี A จำนวน 1,000 บาท
2. **เพิ่มเงิน** เข้าบัญชี B จำนวน 1,000 บาท

ทีนี้ลองจินตนาการว่า... หลังจากขั้นตอนที่ 1 (หักเงินจาก A) ทำงานเสร็จ แต่จู่ๆ ไฟดับ หรือระบบล่ม (crash) ก่อนที่ขั้นตอนที่ 2 จะทำงาน

ผลลัพธ์คือ: **เงินหายไปจากบัญชี A แต่ไม่เคยไปถึงบัญชี B เลย** — เงิน 1,000 บาทนั้นหายไปจากโลกนี้อย่างงงๆ

นี่คือปัญหาที่ **Transaction (ธุรกรรม)** ถูกสร้างมาเพื่อแก้ไข

### นิยามของ Transaction

> **Transaction คือ การรวมหลายขั้นตอนการทำงานเข้าเป็น "ก้อนเดียว" โดยมีกฎว่า:**
> - **ถ้าทุกขั้นตอนสำเร็จหมด → บันทึกผลทั้งหมด (commit = ยืนยันบันทึกจริง)**
> - **ถ้ามีขั้นตอนไหนพังแม้แต่ขั้นตอนเดียว → ยกเลิกทุกอย่างทั้งหมด กลับไปเหมือนไม่เคยทำอะไรเลย (rollback = ย้อนกลับ/ล้างข้อมูล)**

พูดง่ายๆ คือหลักการ **"ทำให้ครบ หรือไม่ทำเลย" (All or Nothing)**

ไม่มีคำว่า "ทำไปครึ่งทาง" — เพราะข้อมูลที่ค้างอยู่ครึ่งทางแบบนั้น อันตรายมากในระบบจริง (เช่น มีลูกค้าจ่ายเงินแล้วแต่ไม่มีออเดอร์ หรือมีออเดอร์แต่ไม่มีการหักสต็อกสินค้า)

---

## บทที่ 2: หลักการ ACID (หัวใจของ Transaction)

ACID เป็นตัวย่อของ 4 คุณสมบัติที่ Transaction ที่ดีต้องมี ผมจะอธิบายทีละตัวด้วยตัวอย่างเดียวกันคือ **การโอนเงิน**

### A — Atomicity (อะตอมมิซิตี้ = ความเป็นหนึ่งเดียว แบ่งแยกไม่ได้)

> คำว่า "Atomic" มาจาก "atom" (อะตอม) ที่แปลว่าสิ่งที่เล็กที่สุดจนแบ่งต่อไม่ได้แล้ว — ในที่นี้หมายถึง transaction ต้องถูกมองเป็น "หนึ่งก้อนเดียว" ไม่สามารถทำสำเร็จแค่บางส่วนได้

**ตัวอย่าง:** การโอนเงิน (หักบัญชี A + เพิ่มบัญชี B) ต้องสำเร็จทั้งคู่ หรือไม่ก็ล้มเหลวทั้งคู่ ห้ามมีกรณี "หักบัญชี A สำเร็จ แต่เพิ่มบัญชี B ไม่สำเร็จ"

### C — Consistency (คอนซิสเทนซี = ความสอดคล้อง/ความถูกต้องตรงกัน)

> หมายถึง ก่อนและหลัง transaction ข้อมูลในระบบต้อง "ถูกต้องตามกฎ" เสมอ

**ตัวอย่าง:** ถ้าระบบกำหนดกฎว่า "ยอดเงินในบัญชีห้ามติดลบ" แล้ว transaction การโอนเงินทำให้บัญชี A ติดลบ ระบบต้องไม่ยอมให้ transaction นั้นสำเร็จ (ต้อง rollback)

### I — Isolation (ไอโซเลชัน = การแยกตัว/ไม่ก้าวก่ายกัน)

> หมายถึง ถ้ามีหลาย transaction เกิดขึ้น "พร้อมกัน" (concurrent = เกิดขึ้นพร้อมกันในเวลาเดียวกัน) แต่ละ transaction ต้องไม่ไปรบกวนหรือเห็นข้อมูลที่ยังไม่เสร็จของอีก transaction หนึ่ง

**ตัวอย่าง:** สมมติคุณกับแฟนใช้บัญชีร่วมกัน แล้วทั้งคู่กดโอนเงินออกพร้อมกันในเวลาเดียวกันเป๊ะๆ ระบบต้องจัดการไม่ให้เกิดความสับสน เช่น ทั้งคู่เห็นยอดเงินคนละแบบ หรือหักเงินซ้ำซ้อนผิดพลาด

*(เดี๋ยวเราจะพูดเรื่องนี้ลึกขึ้นในบทที่ 5 เรื่อง Isolation Level)*

### D — Durability (ดูราบิลิตี้ = ความคงทนถาวร)

> หมายถึง เมื่อ transaction "commit" (ยืนยันบันทึกสำเร็จ) แล้ว ข้อมูลนั้นต้อง **อยู่ถาวร** แม้ระบบจะแครช (crash = ระบบล่ม/หยุดทำงานกะทันหัน) หรือไฟดับทันทีหลังจากนั้น

**ตัวอย่าง:** พอโอนเงินสำเร็จ ระบบแจ้งว่า "โอนสำเร็จ" แล้ว ต่อให้เซิร์ฟเวอร์ดับไปในวินาทีถัดมา เงินที่โอนไปก็ต้องยังอยู่ในบัญชีปลายทาง ไม่หายไปไหน

---

## บทที่ 3: ทำไม Transaction ไม่ใช่แค่เรื่องของ Database

ประเด็นที่สำคัญมากคือ:

- **Database (ฐานข้อมูล) เป็นผู้ที่ "ทำ" transaction ให้จริงๆ** (มันรู้แค่เรื่อง table แถวข้อมูล (row) และกฎบังคับ (constraint))
- **แต่คนที่ "ตัดสินใจ" ว่าอะไรควรอยู่ใน transaction เดียวกัน คือโปรแกรมเมอร์ (คุณ)**

Database ไม่รู้จักคำว่า "ออเดอร์" หรือ "การโอนเงิน" หรอกครับ มันรู้แค่ว่ามีการเขียน/แก้ไขแถวข้อมูลในตาราง ส่วนที่ต้องตัดสินใจว่า "การบันทึกออเดอร์ + การหักสต็อก + การบันทึกการจ่ายเงิน ควรอยู่ใน transaction เดียวกัน" นั้นเป็นหน้าที่ของแอปพลิเคชัน (โปรแกรมที่คุณเขียน) — และนี่คือเหตุผลที่ Spring มีเครื่องมือช่วยเรื่องนี้โดยเฉพาะ

---

## บทที่ 4: `@Transactional` คืออะไร และทำงานยังไงจริงๆ

### วิธีจัดการ Transaction มี 2 แบบ

**แบบที่ 1: Programmatic Transaction (โปรแกรมเมติก = เขียนโค้ดควบคุมเอง)**

คุณต้องเขียนโค้ดสั่ง "เริ่ม transaction" "commit" "rollback" ด้วยตัวเองทุกครั้ง

```java
TransactionStatus status = transactionManager.getTransaction(definition);
try {
    // โค้ดทำงานจริง
    transactionManager.commit(status); // ยืนยันบันทึก
} catch (Exception ex) {
    transactionManager.rollback(status); // ยกเลิกทั้งหมด
    throw ex;
}
```

ข้อเสีย: โค้ดยาว วุ่นวาย ต้องเขียนซ้ำๆ ทุกที่ที่ต้องการ transaction

**แบบที่ 2: Declarative Transaction (เดคเคลอเรทีฟ = ประกาศไว้ล่วงหน้า)**

นี่คือวิธีที่นิยมที่สุด — คุณแค่ "แปะป้าย" `@Transactional` ไว้บน method แล้วปล่อยให้ Spring จัดการทุกอย่างให้เอง

```java
@Transactional
public void placeOrder() {
    // โค้ดทำงานจริง ไม่ต้องเขียนอะไรเกี่ยวกับ transaction เลย
}
```

Spring จะจัดการให้อัตโนมัติ:
- เริ่ม transaction ก่อน method ทำงาน
- ถ้า method จบแบบไม่มีปัญหา → commit (บันทึกจริง)
- ถ้า method throw exception (ข้อผิดพลาดที่เกิดขึ้นระหว่างทำงาน) → rollback (ยกเลิกทั้งหมด)

> 👉 **90% ของโปรเจกต์จริงใช้แบบที่ 2 (Declarative) เพราะสะดวกและโค้ดสะอาดกว่ามาก**

---

### เบื้องหลัง `@Transactional` ทำงานยังไง? (ส่วนที่สำคัญที่สุด — ต้องเข้าใจให้ได้)

หลายคนเข้าใจผิดว่า `@Transactional` จะไป "แก้ไข" โค้ดในเมธอดของเรา — **ความจริงไม่ใช่แบบนั้นเลย**

สิ่งที่ Spring ทำจริงๆ คือ:

### กลไก Proxy (พร็อกซี = ตัวแทน/คนกลาง)

ลองนึกภาพว่า Proxy คือ **"ยามหน้าประตู"** หรือ **"เลขาส่วนตัว"** ที่คอยดักรับทุกคนที่จะเข้ามาหาคุณ

ปกติถ้าไม่มี proxy: คนเดินเข้ามาหาคุณตรงๆ เลย
```
คนเรียก → คุณ (method ทำงานทันที)
```

แต่พอมี `@Transactional` Spring จะสร้าง **"ตัวแทนปลอม"** (proxy object) ขึ้นมาครอบตัวจริงของคุณไว้:
```
คนเรียก → Proxy (ยามหน้าประตู) → เข้าไปเปิด transaction ก่อน → คุณ (method ทำงานจริง) → Proxy ปิด/commit transaction
```

**สิ่งที่ Proxy ทำจริงๆ ตามลำดับ:**
1. **ดักจับ (intercept)** การเรียก method ที่มี `@Transactional`
2. **เปิด transaction** ก่อนที่ method จริงจะเริ่มทำงาน
3. ปล่อยให้ method จริงทำงาน
4. ถ้าจบแบบไม่มีปัญหา → **สั่ง commit**
5. ถ้ามี exception → **สั่ง rollback**

กลไกนี้เรียกว่า **AOP (Aspect-Oriented Programming)** = การเขียนโปรแกรมแบบแยกส่วน "งานหลัก" (business logic) ออกจาก "งานเสริม" (เช่น transaction, logging) โดยไม่ต้องเอามาปนกันในโค้ดเดียวกัน

> **ทำไมเรื่อง Proxy ถึงสำคัญมาก?**
> เพราะ **การเรียก method จะเป็น transaction ได้ก็ต่อเมื่อ "ผ่านตัว Proxy" เท่านั้น** — ถ้าเรียกข้ามหัวตัว Proxy ไปตรงๆ (เช่นเรียกจากใน class เดียวกันเอง) `@Transactional` จะ**ไม่ทำงานเลย แบบไม่มีแจ้งเตือนด้วย** เดี๋ยวเราจะเจอปัญหานี้ในบทที่ 7

---

### `@Transactional` ควรใส่ตรงไหน?

โครงสร้างโปรแกรม Spring Boot ทั่วไปแบ่งเป็นชั้น (layer) ดังนี้:

| Layer | หน้าที่ | ควรใส่ `@Transactional` ไหม |
|---|---|---|
| **Controller** (ตัวรับ request จากผู้ใช้) | รับคำขอเข้ามา ส่งต่อให้ Service | ❌ ไม่ควร |
| **Service** (ตัวจัดการ logic ทางธุรกิจ) | ประมวลผลตามกฎธุรกิจ | ✅ ตรงนี้แหละ! |
| **Repository** (ตัวคุยกับ database) | บันทึก/อ่าน/ลบข้อมูลใน DB | ⚠️ โดยปกติไม่ควร ใส่ต่ำเกินไป |

**เหตุผลที่ควรใส่ที่ Service:** เพราะ Service คือชั้นที่รู้ว่า "ขั้นตอนไหนบ้างที่ควรอยู่ในธุรกรรมเดียวกัน" (เช่น รู้ว่า "สร้างออเดอร์" ต้องทำคู่กับ "บันทึกการจ่ายเงิน")

---

## บทที่ 5: ค่า Attribute (คุณสมบัติ) ต่างๆ ของ `@Transactional`

`@Transactional` ไม่ใช่แค่เปิด-ปิดอย่างเดียว มันมีค่าปรับตั้งได้หลายแบบ

### 5.1 Propagation (โพรพาเกชัน = การส่งต่อ/แพร่กระจายของ transaction)

คำถามคือ: **"ถ้า method A ที่มี `@Transactional` ไปเรียก method B ที่มี `@Transactional` เหมือนกัน จะเกิดอะไรขึ้น? B จะใช้ transaction เดียวกับ A หรือแยกกันคนละอัน?"** — นี่คือสิ่งที่ Propagation ตอบ

มาดูแบบที่ใช้บ่อยที่สุด 2 แบบก่อน:

#### `REQUIRED` (ค่าเริ่มต้น / default)

> ความหมาย: "**ถ้ามี transaction เปิดอยู่แล้ว → เข้าร่วมด้วยกันเลย** (ใช้อันเดียวกัน) **ถ้ายังไม่มี → สร้างใหม่**"

ลองนึกภาพเป็นการ**นั่งรถคันเดียวกัน** — ถ้ามีรถวิ่งอยู่แล้ว (transaction A เปิดอยู่) แล้วมีคนขึ้นรถมาด้วย (method B ถูกเรียก) ก็จะนั่งรถคันเดียวกันไปด้วยกัน (ใช้ transaction เดียวกัน) ถ้ารถคว่ำ (เกิด error) ทุกคนในรถคันนั้นเจ๊งไปด้วยกันหมด (rollback ทั้งหมด)

```java
@Transactional
public void placeOrder() {
    orderService.saveOrder();   // ใช้ transaction เดียวกัน
    paymentService.charge();    // ใช้ transaction เดียวกัน
}
```
ถ้า `charge()` (การเก็บเงิน) ล้มเหลว → **ทุกอย่าง rollback หมด** รวมถึง `saveOrder()` ด้วย เพราะอยู่ใน transaction เดียวกัน

#### `REQUIRES_NEW` (บังคับสร้างใหม่เสมอ)

> ความหมาย: "**ไม่สนใจว่ามี transaction เปิดอยู่หรือไม่ ให้พัก (suspend) transaction เดิมไว้ก่อน แล้วสร้าง transaction ใหม่แยกต่างหาก**"

ลองนึกภาพเป็น**การขึ้นรถคนละคัน** — แม้จะไปที่เดียวกัน แต่ถ้ารถคันหนึ่งคว่ำ อีกคันก็ยังวิ่งต่อไปได้ปกติ ไม่เกี่ยวข้องกัน

**ใช้เมื่อไหร่:** เหมาะกับสิ่งที่ **"ต้องถูกบันทึกไว้เสมอ แม้ธุรกรรมหลักจะล้มเหลว"** เช่น การเก็บ log (บันทึกประวัติ) ว่ามีคนพยายามทำอะไร

```java
@Transactional
public void placeOrder() {
    orderRepository.save(order);           // transaction หลัก
    auditService.logOrderAttempt(order);    // REQUIRES_NEW → transaction แยก
    throw new RuntimeException("จ่ายเงินไม่สำเร็จ");
}
```

```
placeOrder()
│
│ Transaction A
│
├── save Order
│
└── logOrderAttempt()
          │
          │ "ขอ Transaction ใหม่"
          ▼
       Transaction B
          │
          └── save Log
          │
          └── Commit B (save)
          
กลับมาที่ Transaction A
│
└── throw Error
       ↓
   Rollback A (error)
```
ผลลัพธ์: **ออเดอร์ → ถูก rollback (ไม่บันทึก)** แต่ **log การพยายามสั่งซื้อ → ถูก commit (บันทึกไว้)** เพราะอยู่คนละ transaction กัน

#### ตัวอื่นๆ (ใช้น้อยกว่า แต่ควรรู้จักไว้)

| ชื่อ | ความหมายแบบเข้าใจง่าย |
|---|---|
| `SUPPORTS` | ถ้ามี transaction อยู่แล้วก็เข้าร่วม แต่ถ้าไม่มีก็ทำงานแบบไม่มี transaction เลย (ไม่บังคับ) |
| `MANDATORY` (บังคับ) | ต้องมี transaction อยู่แล้วเท่านั้นถึงจะทำงานได้ ถ้าไม่มีจะ error ทันที |
| `NOT_SUPPORTED` | สั่งให้พัก transaction ที่มีอยู่ไว้ก่อน แล้วรันแบบไม่มี transaction |
| `NEVER` (ห้ามเด็ดขาด) | ถ้ามี transaction อยู่แล้วจะ error ทันที ต้องไม่มี transaction เท่านั้น |
| `NESTED` (ซ้อนกัน) | สร้าง "จุด save (savepoint)" ไว้ข้างในธุรกรรมเดิม ทำให้ rollback แค่บางส่วนได้ (ขึ้นกับว่า database รองรับไหม) |

> 👉 ในการทำงานจริง ใช้แค่ `REQUIRED` กับ `REQUIRES_NEW` ก็ครอบคลุมสถานการณ์ส่วนใหญ่แล้วครับ

---

### 5.2 Isolation (ไอโซเลชันเลเวล = ระดับการแยกตัวของธุรกรรม)

นี่คือการตอบคำถามว่า: **"ถ้ามีหลาย transaction ทำงานพร้อมกัน แต่ละอันจะ "เห็น" ข้อมูลที่อีกอันกำลังแก้ไขอยู่ (แต่ยังไม่ commit) ได้แค่ไหน?"**

ลองนึกภาพร้านค้าที่มีพนักงาน 2 คนกำลังนับสต็อกสินค้าพร้อมกัน — ระดับ Isolation คือกฎว่าพนักงานคนหนึ่งจะเห็นตัวเลขที่อีกคนกำลังแก้ไข (แต่ยังนับไม่เสร็จ) ได้หรือไม่

| ระดับ | อธิบายแบบบ้านๆ | ความปลอดภัย | ความเร็ว |
|---|---|---|---|
| **READ_UNCOMMITTED** (อ่านได้แม้ยังไม่ยืนยัน) | เห็นข้อมูลที่อีกฝั่งยังทำไม่เสร็จได้เลย เสี่ยงเห็นข้อมูลผิดๆ (dirty read = อ่านข้อมูลสกปรก/ยังไม่เป็นทางการ) | ต่ำสุด | เร็วสุด |
| **READ_COMMITTED** (อ่านเฉพาะที่ยืนยันแล้ว) | เห็นเฉพาะข้อมูลที่อีกฝั่ง commit แล้วเท่านั้น — เป็นค่าที่นิยมใช้บ่อยที่สุด | ปานกลาง | เร็ว |
| **REPEATABLE_READ** (อ่านซ้ำได้ผลเหมือนเดิม) | ถ้าอ่านข้อมูลแถวเดิมซ้ำๆ ในธุรกรรมเดียวกัน ผลลัพธ์ต้องเหมือนเดิมทุกครั้ง ไม่เปลี่ยนกลางทาง | สูง | ช้าลง |
| **SERIALIZABLE** (เรียงลำดับทีละอัน) | ปลอดภัยสูงสุด เหมือนบังคับให้ทุก transaction ทำงานทีละคิว ไม่มีการทำพร้อมกันจริงๆ | สูงสุด | ช้าสุด |

> 👉 **กฎง่ายๆ:** ยิ่งปลอดภัยมาก ยิ่งช้าลง (เพราะต้องล็อกข้อมูลรอคิว) โดยทั่วไปให้ใช้ค่า default ของ database ไปก่อน (ส่วนใหญ่คือ READ_COMMITTED) แล้วค่อยปรับเพิ่มเมื่อเจอปัญหาจริงๆ เท่านั้น

---

### 5.3 Rollback Rules (กฎการ Rollback) — จุดที่คนพลาดกันบ่อยที่สุด!

ก่อนอื่นต้องเข้าใจคำว่า **Exception (เอ็กเซปชัน)** ก่อน — มันคือ **"ข้อผิดพลาดที่เกิดขึ้นระหว่างโปรแกรมทำงาน"** เช่น หารด้วยศูนย์ หรือพยายามเข้าถึงข้อมูลที่ไม่มีอยู่จริง

ใน Java, Exception แบ่งเป็น 2 แบบหลักๆ:

- **Unchecked Exception (ไม่บังคับตรวจสอบ)** — เช่น `RuntimeException` — เป็น error ที่มักเกิดจาก bug ในโค้ด ไม่บังคับให้เขียนโค้ดดักจับ
- **Checked Exception (บังคับตรวจสอบ)** — เช่น `Exception` ทั่วไป — เป็น error ที่ Java บังคับให้โปรแกรมเมอร์ต้องเขียนโค้ดจัดการไว้ล่วงหน้า (เช่นด้วยคำว่า `throws`)

### กฎเริ่มต้นของ Spring (ที่คนไม่รู้แล้วเจอปัญหาบ่อยมาก!)

> **Spring จะ rollback (ยกเลิกธุรกรรม) ให้อัตโนมัติ ก็ต่อเมื่อเจอ Unchecked Exception เท่านั้น!**
>
> **ถ้าเป็น Checked Exception → Spring จะ "ไม่ rollback ให้" แม้ว่าโปรแกรมจะ error ก็ตาม!**

ลองดูโค้ดนี้:
```java
@Transactional
public void placeOrder() throws Exception {
    orderRepository.save(order);
    throw new Exception("เกิดข้อผิดพลาด"); // เป็น Checked Exception
}
```
แม้จะมีการ `throw` error ออกมา แต่เพราะมันเป็น **Checked Exception** → **Spring จะ commit ข้อมูลตามปกติ ไม่ rollback ให้!** — นี่คือกับดักที่อันตรายมาก เพราะโปรแกรมดูเหมือนจะพังแต่ข้อมูลกลับถูกบันทึกไปแล้ว

### วิธีแก้ไข

**วิธีที่ 1: บอก Spring ให้ rollback ทุก Exception แบบชัดเจน**
```java
@Transactional(rollbackFor = Exception.class)
```
ป้ายกำกับนี้บอกว่า "ไม่ว่าจะเป็น exception แบบไหน ให้ rollback หมด"

**วิธีที่ 2 (แนะนำ): ใช้ Unchecked Exception ตั้งแต่แรก**

สร้าง exception ของตัวเองที่เป็น unchecked (สืบทอดมาจาก `RuntimeException`)
```java
public class PaymentFailedException extends RuntimeException {
}

@Transactional
public void placeOrder() {
    orderRepository.save(order);
    throw new PaymentFailedException(); // เป็น unchecked → rollback อัตโนมัติ ไม่ต้องตั้งค่าอะไรเพิ่ม
}
```

> 👉 **คำแนะนำ:** ในโปรเจกต์จริงส่วนใหญ่แนะนำให้ error ที่เกี่ยวกับธุรกิจ (business exception) เป็น unchecked exception ทั้งหมด เพื่อให้ rollback อัตโนมัติโดยไม่ต้องคอยตั้งค่าเพิ่ม ลดโอกาสลืม

---

### 5.4 `readOnly` (อ่านอย่างเดียว)

```java
@Transactional(readOnly = true)
```
ใช้เมื่อ method นั้น **แค่อ่านข้อมูล ไม่มีการแก้ไข/บันทึกอะไรเลย** เช่น method ค้นหาข้อมูล การใส่ค่านี้จะช่วยให้ database ทำงานได้เร็วขึ้นเล็กน้อย (เพราะไม่ต้องเตรียมพร้อมสำหรับการเขียนข้อมูล) และยังช่วยให้คนอ่านโค้ดเข้าใจ intent (เจตนา) ของ method นั้นด้วยว่ามันไม่แก้ไขข้อมูลแน่นอน

### 5.5 `timeout` (เวลาที่จำกัด)

```java
@Transactional(timeout = 5)
```
ถ้า transaction ทำงานนานเกิน 5 วินาที → **ถูกยกเลิก (rollback) อัตโนมัติ** ป้องกันไม่ให้ transaction ค้างนานเกินไปจนทำให้ระบบอืด

---

## บทที่ 6: ข้อผิดพลาดที่คนเจอบ่อยที่สุด (Pitfalls)

ส่วนนี้สำคัญมาก เพราะปัญหาพวกนี้ **"ไม่มี error แจ้งเตือน"** เลย โปรแกรมจะรันได้ปกติ แต่ transaction จะไม่ทำงานตามที่คิดไว้ — อันตรายมากในโปรเจกต์จริง

### 6.1 Self-Invocation (การเรียกตัวเองในคลาสเดียวกัน) — ปัญหาที่ดังที่สุด

จำเรื่อง **Proxy (ยามหน้าประตู)** จากบทที่ 4 ได้ไหมครับ? ปัญหานี้เกิดจากตรงนั้นเป๊ะๆ

```java
@Service
public class OrderService {

    public void placeOrder() {
        saveOrder(); // ❌ เรียกตรงๆ ในคลาสเดียวกัน — ไม่ผ่าน Proxy!
    }

    @Transactional
    public void saveOrder() {
        orderRepository.save(order);
    }
}
```

**ทำไมถึงพัง:** เพราะการเรียก `saveOrder()` จากใน `placeOrder()` เป็นการเรียก**ตรงๆ ในตัวเอง (this)** ไม่ได้เดินผ่าน "ยามหน้าประตู" (Proxy) เลย — เหมือนคุณเดินอ้อมไปเข้าประตูหลังบ้านแทนที่จะเข้าประตูหน้าที่มียามยืนอยู่

**ผลลัพธ์:** `@Transactional` **ถูกเมิน (ignored) ไปเฉยๆ โดยไม่มี error หรือคำเตือนใดๆ ทั้งสิ้น**

**วิธีแก้:** ย้าย method ที่ต้องการ transaction ไปไว้ใน class อื่น แล้วเรียกจากข้างนอกแทน

```java
@Service
public class OrderPersistenceService {

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
```

### 6.2 `@Transactional` บน Private Method — ใช้ไม่ได้เลย

```java
@Transactional
private void saveOrder() {  // ❌ private
    ...
}
```

**เหตุผล:** Proxy ของ Spring ดักจับได้แค่ method ที่เป็น **public (สาธารณะ / เรียกจากที่ไหนก็ได้)** เท่านั้น ถ้าเป็น `private` (ส่วนตัว เรียกได้แค่ในคลาสเดียวกัน) หรือ `protected`/`package-private` Proxy จะมองไม่เห็นเลย

> 👉 **กฎเหล็ก: Method ที่ใส่ `@Transactional` ต้องเป็น `public` เท่านั้น**

### 6.3 การจับ Exception เอง แล้วดันไปกลืนมันเงียบๆ

```java
@Transactional
public void placeOrder() {
    try {
        paymentService.charge();
    } catch (PaymentFailedException ex) {
        log.error("จ่ายเงินไม่สำเร็จ", ex);   // แค่ log ไว้ ไม่ throw ต่อ
    }
}
```

**ปัญหา:** โค้ดนี้ดูเหมือนจะจัดการ error ได้ดี (มีการ log ไว้ด้วย) แต่ปัญหาคือ Spring จะ rollback ก็ต่อเมื่อ **exception "หลุดออกไป" นอก method** เท่านั้น พอเรา `catch` (ดักจับ) ไว้แล้วไม่ throw ต่อ Spring จะคิดว่า "method นี้จบแบบไม่มีปัญหา" แล้วก็ **commit ให้ปกติ** ทั้งที่จริงๆ การจ่ายเงินล้มเหลว!

**วิธีแก้:** ต้อง throw exception ต่อออกไป หรือสั่ง rollback มือ:
```java
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
```
แต่ส่วนใหญ่แนะนำให้ throw exception ต่อไปเลย จะง่ายและปลอดภัยกว่า

### 6.4 การยัดงานที่ไม่เกี่ยวกับ Database เข้าไปใน Transaction

```java
@Transactional
public void placeOrder() {
    orderRepository.save(order);
    emailService.sendConfirmationEmail(); // ❌ ไม่ควรอยู่ในนี้
}
```

**ปัญหา:** การส่งอีเมลมักจะ**ช้า**และ**ไม่แน่นอน** (เน็ตอาจล่ม เซิร์ฟเวอร์เมลอาจดาวน์) ถ้าเราเอามารวมกับ transaction ของ database แล้วอีเมลส่งไม่สำเร็จ คำถามคือ: **เราอยากให้การบันทึกออเดอร์ทั้งหมด rollback ไปด้วยเพราะแค่ส่งอีเมลไม่ได้จริงหรือ?** — ส่วนใหญ่คำตอบคือ "ไม่อยากให้เป็นแบบนั้น"

**แนวทางที่ดีกว่า:** แยกงานที่ไม่ใช่ database ออกจาก transaction โดยใช้ event (เหตุการณ์) หรือ async (การทำงานแบบไม่รอผล ที่จะอธิบายในบทถัดไป)

### 6.5 `UnexpectedRollbackException` (ข้อผิดพลาดที่งงที่สุด)

ถ้าเจอ error แบบนี้:
```
UnexpectedRollbackException: Transaction silently rolled back
```
แปลว่า: มี transaction ย่อยที่ซ้อนอยู่ข้างในถูก "มาร์ค (mark)" ไว้ว่าต้อง rollback แล้ว แต่ตัว transaction แม่ข้างนอกดันพยายามจะ commit — Spring เลยโวยวายออกมา

**วิธีสืบสาเหตุ:** อย่ามองที่จุด commit แต่ให้ย้อนกลับไปหาว่า "จุดไหนที่สั่ง rollback ไว้ก่อนหน้านี้" (มักเกิดจากข้อ 6.3 ที่กลืน exception ไว้ในธุรกรรมย่อย)

### 6.6 Transaction ที่ทำงานนานเกินไป

**สาเหตุที่พบบ่อย:**
- ทำงานที่ต้องรอ (I/O) อยู่ในธุรกรรม เช่น เรียก API ภายนอก
- รอผู้ใช้กรอกข้อมูล (จริงๆ มีคนทำแบบนี้จริง!)

**ผลเสีย:** ธุรกรรมที่ทำงานนาน จะ **ล็อกแถวข้อมูล (lock rows)** ไว้นานเกินไป ทำให้คนอื่นที่ต้องการใช้ข้อมูลเดียวกันต้องรอคิวนาน และเสี่ยงเกิด **deadlock** (การล็อกข้อมูลค้างจนสองฝั่งรอกันไม่จบ)

> **กฎ:** ธุรกรรมควรสั้นที่สุดเท่าที่จำเป็น ไม่ยาวไปกว่านั้น

---

## บทที่ 7: `@Transactional` + `@Async` = คู่อันตราย

**`@Async`** คือ ป้ายกำกับที่บอกว่า "ให้ method นี้ทำงานแบบไม่ต้องรอ (asynchronous = ไม่ประสานเวลา)" คือมันจะไปทำงานใน **thread อื่น** (thread = เส้นทางการทำงานคู่ขนาน คิดง่ายๆ ว่าเป็นเหมือน "คนงาน" อีกคนที่แยกไปทำงานพร้อมกันโดยไม่ต้องรอกัน)

### ปัญหาหลัก

> **Transaction ผูกติดกับ Thread เดียวเท่านั้น (thread-bound)**

พอ `@Async` ทำให้ method ไปทำงานคนละ thread — มันจะ **ไม่มี transaction ติดตัวไปด้วยเลย** (เว้นแต่จะระบุ `@Transactional` ไว้ที่ method นั้นแยกต่างหาก ซึ่งจะกลายเป็นคนละ transaction กันอยู่ดี)

```java
@Transactional
public void placeOrder() {
    orderRepository.save(order);
    asyncService.sendConfirmationEmail(order); // ไปคนละ thread คนละ transaction ทันที
}
```

**ความเข้าใจผิดที่พบบ่อยที่สุด:** คนมักคิดว่า "ถ้า async method ถูกเรียกจาก transactional method มันจะร่วม transaction เดียวกันด้วย" — **ผิดครับ ไม่มีทางเกิดขึ้นแบบนั้นเลย**

### ตัวอย่างที่อันตราย

```java
@Transactional
public void placeOrder() {
    orderRepository.save(order);
    asyncService.notifyWarehouse(order);   // แจ้งคลังสินค้าแบบ async
    throw new RuntimeException("จ่ายเงินไม่สำเร็จ");
}
```

**ผลลัพธ์ที่เกิดขึ้นจริง:**
- ออเดอร์ → **rollback** (ไม่ถูกบันทึก)
- แต่ async method → **ยังทำงานต่อไปตามปกติ**
- **คลังสินค้าถูกแจ้งเตือนว่ามีออเดอร์ ทั้งที่จริงๆ ออเดอร์ไม่มีอยู่ในระบบเลย!**

นี่คือจุดกำเนิดของปัญหาข้อมูลไม่ตรงกัน (inconsistency) ในระบบที่ซับซ้อน

### วิธีแก้ที่ปลอดภัยกว่า: Transactional Event (เหตุการณ์ที่ผูกกับ transaction)

Spring มีกลไกพิเศษที่ปลอดภัยกว่ามาก:
```java
@Transactional
public void placeOrder() {
    orderRepository.save(order);
    applicationEventPublisher.publishEvent(new OrderPlacedEvent(order));
}

@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void onOrderPlaced(OrderPlacedEvent event) {
    sendEmail(event);
}
```

ความมหัศจรรย์ของโค้ดนี้: **event `onOrderPlaced` จะทำงานก็ต่อเมื่อ transaction หลัก commit สำเร็จเท่านั้น** ถ้า transaction หลัก rollback ไป event นี้จะไม่ทำงานเลย — ปลอดภัยกว่ามาก ไม่มี "ผี" (side effect ที่เกิดขึ้นทั้งที่ไม่ควรเกิด) แบบตัวอย่างก่อนหน้า

> **กฎง่ายๆ ที่ควรจำ:** อย่าคิดว่างานแบบ async จะเป็นส่วนหนึ่งของ transaction เด็ดขาด — ถ้าต้องการความถูกต้อง ให้ทำ transaction ให้เสร็จก่อน แล้วค่อยสั่งงาน async ทีหลัง

---

## บทที่ 8: การ Debug (แก้บั๊ก) ปัญหาเรื่อง Transaction

ปัญหาเรื่อง transaction มักจะ**ไม่มี error แจ้งเตือนเลย** — โปรแกรมรันได้ปกติ แต่ข้อมูลผิดเพี้ยนไปเรื่อยๆ นี่คือเหตุผลที่ **Logging (การบันทึก log = ข้อความบอกสถานะการทำงานของระบบ)** สำคัญมาก

### เปิด Log ของ Transaction

ใส่ในไฟล์ `application.yml`:
```yaml
logging:
  level:
    org.springframework.transaction: DEBUG
```

เมื่อเปิดแล้วจะเห็น log แบบนี้:
```
Creating new transaction with name [OrderService.placeOrder]
Participating in existing transaction
Committing JDBC transaction
Rolling back JDBC transaction
```
Log พวกนี้จะบอกได้ว่า transaction เริ่มตรงไหน ถูก commit หรือ rollback เมื่อไหร่

### เปิด Log ของ SQL (คำสั่งที่ยิงไปที่ database จริงๆ)

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
```
ช่วยให้เห็นว่าจริงๆ แล้วมีคำสั่งอะไรถูกส่งไปที่ database บ้าง เอาไว้เทียบกับจุดที่ commit/rollback ได้

### หลักการ Debug ที่แนะนำ

1. เปิด transaction log
2. เปิด SQL log
3. หาว่าจุดไหนที่ transaction เริ่มต้น
4. ตามดูว่า exception เกิดที่ไหน และหลุดออกจาก method หรือไม่
5. เช็คเวลาที่ commit/rollback เกิดขึ้นจริง

---

## บทที่ 9: ทางเลือกในการเขียนโค้ด — Programmatic Transaction (แบบเขียนควบคุมเอง)

ถ้าบางครั้งอยากได้ความยืดหยุ่นมากขึ้น เช่น อยากกำหนดเองว่าจะ commit ตอนไหน สามารถใช้ programmatic ได้ มี 2 วิธี

### วิธีที่ 1: ใช้ `PlatformTransactionManager` โดยตรง

`PlatformTransactionManager` คือ **ตัวกลางที่ Spring ใช้ควบคุมการเริ่ม/commit/rollback ธุรกรรม** จริงๆ (ไม่ว่าจะใช้ฐานข้อมูลแบบไหนก็ตาม เช่น JDBC, JPA)

```java
TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

try {
    withdraw(from, amount, fee);   // หักเงินบัญชีต้นทาง
    deposit(to, amount);           // เพิ่มเงินบัญชีปลายทาง
    transactionManager.commit(transactionStatus);   // ยืนยันบันทึก
} catch (RuntimeException e) {
    transactionManager.rollback(transactionStatus);  // ยกเลิกทั้งหมด
    throw e;
}
```

### วิธีที่ 2: ใช้ `TransactionTemplate` (แบบ template สำเร็จรูป)

คล้ายกับ `JdbcTemplate` ที่ Spring มีให้ใช้อยู่แล้ว — ช่วยลดโค้ดที่ต้องเขียนซ้ำๆ

```java
transactionTemplate.execute(new TransactionCallbackWithoutResult() {
    public void doInTransactionWithoutResult(TransactionStatus status) {
        try {
            withdraw(from, amount, fee);
            deposit(to, amount);
        } catch (NoSuchElementException exception) {
            status.setRollbackOnly();   // สั่งให้ rollback
        }
    }
});
```

> 👉 **สรุป:** ใช้ Declarative (`@Transactional`) ในกรณีทั่วไป 90% ของเวลา ส่วน Programmatic ใช้เฉพาะกรณีที่ต้องการควบคุมพิเศษจริงๆ เท่านั้น เพราะมันทำให้โค้ด transaction ไปปนกับ business logic (logic ทางธุรกิจ) ซึ่งดูแลรักษายากกว่า

---

## บทที่ 10: Global Transaction vs Local Transaction (ความรู้เสริม)

| ประเภท | ความหมาย |
|---|---|
| **Local Transaction (ธุรกรรมท้องถิ่น)** | ทำงานอยู่ในระบบ/เครื่องเดียว database เดียว — เป็นแบบที่ใช้ในตัวอย่างทั้งหมดที่ผ่านมา ง่ายต่อการทำ |
| **Global Transaction (ธุรกรรมข้ามระบบ)** | ครอบคลุมหลาย resource ข้ามระบบพร้อมกัน เช่น มีทั้ง database หลายตัว + message queue (คิวข้อความสำหรับส่งงานระหว่างระบบ) ต้องมีการประสานงานที่ซับซ้อนกว่ามาก มักเจอในระบบองค์กรใหญ่ที่กระจาย (distributed) หลายเซิร์ฟเวอร์ |

---

## สรุปทุกอย่างที่ต้องจำ (Cheat Sheet)

1. **Transaction = ทำให้ครบทุกขั้นตอน หรือไม่ทำเลย (All or Nothing)**
2. **ACID** = Atomicity (ครบ/ไม่ครบ), Consistency (ถูกต้องตามกฎ), Isolation (ไม่ก้าวก่ายกัน), Durability (ถาวรหลัง commit)
3. `@Transactional` ทำงานผ่าน **Proxy (ยามหน้าประตู)** — ต้องเรียกจาก**นอกคลาส**และเป็น **public method** เท่านั้นถึงจะทำงาน
4. **Propagation** สำคัญที่สุดคือ `REQUIRED` (ใช้ร่วมกัน) และ `REQUIRES_NEW` (แยกกันเด็ดขาด)
5. **Rollback default จะทำงานเฉพาะ Unchecked Exception** — ถ้าใช้ Checked Exception ต้องใส่ `rollbackFor = Exception.class` เอง หรือใช้ unchecked exception ตั้งแต่แรก
6. **ห้าม** กลืน exception เงียบๆ โดยไม่ throw ต่อ — ไม่งั้น transaction จะ commit ทั้งที่ควร rollback
7. **`@Async` ไม่มีทางแชร์ transaction กับ method ที่เรียกมัน** — ใช้ `@TransactionalEventListener(phase = AFTER_COMMIT)` แทนถ้าต้องการความปลอดภัย
8. เปิด log `org.springframework.transaction: DEBUG` เวลา debug ปัญหาที่หาสาเหตุไม่เจอ

---

## คำศัพท์ทั้งหมด (Glossary) — สรุปไว้ให้เปิดดูได้เร็วๆ

| คำศัพท์ | ความหมาย |
|---|---|
| Transaction | ธุรกรรม / กลุ่มการทำงานที่ต้องสำเร็จหรือล้มเหลวไปด้วยกันทั้งหมด |
| Commit | ยืนยันบันทึกข้อมูลจริง |
| Rollback | ยกเลิก/ย้อนข้อมูลกลับ เหมือนไม่เคยทำอะไรเลย |
| ACID | 4 คุณสมบัติของ transaction ที่ดี |
| Atomicity | ความเป็นหนึ่งเดียว แบ่งแยกไม่ได้ |
| Consistency | ความสอดคล้อง ถูกต้องตามกฎเสมอ |
| Isolation | การแยกตัว ไม่ก้าวก่ายกันของธุรกรรมที่ทำงานพร้อมกัน |
| Durability | ความคงทนถาวรหลัง commit |
| Concurrent | เกิดขึ้นพร้อมกันในเวลาเดียวกัน |
| Annotation | ป้ายกำกับบนโค้ด ขึ้นต้นด้วย `@` |
| Method | ฟังก์ชัน/ชุดคำสั่งหนึ่งก้อนในโค้ด |
| Proxy | ตัวแทน/คนกลางที่ดักจับการเรียกใช้งาน |
| AOP | การเขียนโปรแกรมแยกงานหลักออกจากงานเสริม |
| Propagation | การส่งต่อ/แพร่กระจายของธุรกรรมเมื่อมีการเรียกซ้อนกัน |
| Exception | ข้อผิดพลาดที่เกิดขึ้นระหว่างโปรแกรมทำงาน |
| Unchecked Exception | Exception ที่ไม่บังคับต้องดักจับ (rollback อัตโนมัติ) |
| Checked Exception | Exception ที่บังคับต้องดักจับ (ไม่ rollback อัตโนมัติ) |
| Declarative | แบบประกาศ/แปะป้ายไว้ล่วงหน้า ให้เฟรมเวิร์กจัดการเอง |
| Programmatic | แบบเขียนโค้ดควบคุมเอง |
| Async / Asynchronous | ทำงานแบบไม่รอผล ไปทำงานอีก thread หนึ่ง |
| Thread | เส้นทางการทำงานคู่ขนานในโปรแกรม |
| Thread-bound | ผูกติดอยู่กับ thread เดียวเท่านั้น |
| Deadlock | สถานการณ์ที่สอง transaction ล็อกข้อมูลรอกันจนค้างไม่จบ |
| Isolation Level | ระดับการมองเห็นข้อมูลระหว่าง transaction ที่ทำงานพร้อมกัน |
| Dirty Read | การอ่านข้อมูลที่ยังไม่ถูกยืนยัน (commit) จากธุรกรรมอื่น |
| Log / Logging | ข้อความบันทึกสถานะการทำงานของระบบ ใช้เพื่อ debug |

---

*หวังว่าคอร์สนี้จะช่วยให้เข้าใจ Transaction ใน Spring Boot ได้ชัดเจนขึ้นนะครับ ถ้ามีจุดไหนยังงงหรืออยากให้อธิบายเพิ่ม ถามได้เลยครับ*