package com.example.czn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class CznApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        System.out.println("Hello Moto");
        SpringApplication.run(CznApplication.class, args);
    }

}
