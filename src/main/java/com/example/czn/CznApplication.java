package com.example.czn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class CznApplication {

    public static void main(String[] args) {
        String botToken = "1636752449:AAFlrD_g_HBe4DIHjehoIA5E13Adj2nf36I";
        ApiContextInitializer.init();
        System.out.println("Hello Moto");
        SpringApplication.run(CznApplication.class, args);
    }

}
