package com.telehealthpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TeleHealth Pro - Online Doctor Consultation Platform
 * Main entry point of the Spring Boot application.
 */
@SpringBootApplication
public class TeleHealthProApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeleHealthProApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" TeleHealth Pro is running at http://localhost:8080");
        System.out.println("=================================================");
    }
}
