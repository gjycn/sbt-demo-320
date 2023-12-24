package com.example.sbtdemo320.controller;

import com.example.sbtdemo320.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api")
@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/check/lock1")
    public Map<String, String> checkAndLock1() {
        stockService.checkAndLock1();
        return Map.of("msg: ", "v1: 验库存并锁库存成功！");

    }

    @GetMapping("/check/lock2")
    public Map<String, String> checkAndLock2() {
        stockService.checkAndLock2();
        return Map.of("msg: ", "v2: 验库存并锁库存成功！");

    }

    @GetMapping("/check/lock3")
    public Map<String, String> checkAndLock3() {
        stockService.checkAndLock3();
        return Map.of("msg: ", "v3: 验库存并锁库存成功！");

    }
}
