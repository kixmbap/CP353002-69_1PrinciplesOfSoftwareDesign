package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. Running this starts an embedded Tomcat server and
 * registers Spring MVC's DispatcherServlet, which is the component
 * that will later call our custom ViewResolver for every request.
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
