package com.example.sbtdemo320;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class SbtDemo320Application {

    public static void main(String[] args) {
        SpringApplication.run(SbtDemo320Application.class, args);
    }
}
