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

    @GetMapping("/check/lock")
    public Map<String, String> checkAndLock() {
        stockService.checkAndLock3();
        return Map.of("msg: ","验库存并锁库存成功！");

    }
}
