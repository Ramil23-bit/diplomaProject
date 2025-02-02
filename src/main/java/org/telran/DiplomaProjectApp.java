package org.telran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DiplomaProjectApp {
    public static void main(String[] args) {
        SpringApplication.run(DiplomaProjectApp.class, args);
    }
}