package com.example.stockmarket.controller;


import com.example.stockmarket.dto.SetStocksRequest;
import com.example.stockmarket.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService b) {
        this.bankService = b;
    }

    @GetMapping
    public Object getStocks() {
        return Map.of("stocks", bankService.getAllStocks());
    }

    @PostMapping
    public ResponseEntity<?> setStocks(@RequestBody @Valid SetStocksRequest body) {

        if (body == null || body.stocks == null) {
            return ResponseEntity.badRequest().body("stocks is null");
        }

        Map<String, Integer> map = body.stocks.stream()
                .collect(java.util.stream.Collectors.toMap(
                        s -> s.name,
                        s -> s.quantity
                ));

        bankService.setStocks(map);

        return ResponseEntity.ok().build();
    }
}
