package com.example.stockmarket.controller;


import com.example.stockmarket.dto.SetStocksRequest;
import com.example.stockmarket.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public Map<String, Object> getStocks() {
        return Map.of("stocks", bankService.getAllStocks());
    }

    @PostMapping
    public ResponseEntity<?> setStocks(@RequestBody @Valid SetStocksRequest body) {

        Map<String, Integer> map = body.stocks.stream()
                .collect(Collectors.toMap(
                        s -> s.name,
                        s -> s.quantity,
                        (a, b) -> b
                ));

        bankService.setStocks(map);

        return ResponseEntity.ok().build();
    }
}
