package com.example.lab10.client;

import com.example.lab10.model.Product;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// เปิดตัวอย่างด้วย --spring.profiles.active=webclient-demo โดยใช้ port 8080
@Component
@Profile("webclient-demo")
public class ProductWebClientDemo {

    private final ProductWebClient client;

    public ProductWebClientDemo(ProductWebClient client) {
        this.client = client;
    }

    // เริ่มเมื่อเซิร์ฟเวอร์พร้อมรับ HTTP แล้ว และไม่บล็อก thread รอผล
    @EventListener(ApplicationReadyEvent.class)
    public void demonstrateOperators() {
        client.getProductById("1")
                .map(Product::getName)
                .defaultIfEmpty("No product")
                .subscribe(name -> System.out.println("Product: " + name),
                        error -> System.err.println("Product error: " + error.getMessage()));

        // หมวดหมู่ที่ไม่มีข้อมูลคืน Flux ว่าง จึงใช้ defaultIfEmpty ได้
        client.getByCategory("NoSuchCategory")
                .map(Product::getName)
                .defaultIfEmpty("No products in category")
                .subscribe(System.out::println,
                        error -> System.err.println("Category error: " + error.getMessage()));

        // filter เลือกสินค้าที่มีสต็อก และ flatMap ต่อ HTTP request เพื่ออ่านราคาหลังลด
        client.getAllProducts()
                .filter(product -> product.getStock() != null && product.getStock() > 0)
                .flatMap(product -> client.getDiscountedPrice(product.getId())
                        .map(price -> product.getId() + " discounted price: " + price))
                .subscribe(System.out::println,
                        error -> System.err.println("Price error: " + error.getMessage()));
    }
}
