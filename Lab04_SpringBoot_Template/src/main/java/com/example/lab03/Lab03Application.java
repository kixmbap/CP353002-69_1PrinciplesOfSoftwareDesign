package com.example.lab03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * จุดเริ่มต้นของแอปพลิเคชัน Spring Boot
 *
 * @SpringBootApplication รวม 3 annotation ไว้ในตัวเดียว:
 *   - @Configuration       : กำหนดให้ class นี้เป็น Spring config
 *   - @EnableAutoConfiguration : เปิด Auto-configuration
 *   - @ComponentScan       : สแกนหา @Component, @Controller ฯลฯ
 */
@SpringBootApplication
public class Lab03Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab03Application.class, args);
    }
}
