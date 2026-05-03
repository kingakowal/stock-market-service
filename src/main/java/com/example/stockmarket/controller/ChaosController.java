package com.example.stockmarket.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chaos")
public class ChaosController {

    @PostMapping
    public String kill() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            System.exit(1);
        }).start();

        return "Instance shutting down";
    }
}