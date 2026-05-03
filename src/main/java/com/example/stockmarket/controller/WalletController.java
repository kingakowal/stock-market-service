package com.example.stockmarket.controller;

import com.example.stockmarket.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;
    private final BankService bankService;
    private final AuditService auditService;

    public WalletController(WalletService walletService,
                            BankService bankService,
                            AuditService auditService) {
        this.walletService = walletService;
        this.bankService = bankService;
        this.auditService = auditService;
    }


    @PostMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<?> trade(
            @PathVariable String walletId,
            @PathVariable String stockName,
            @RequestBody Map<String, String> body
    ) {

        String type = body.get("type");

        if (!bankService.hasStock(stockName)) {
            return ResponseEntity.status(404).body("Stock does not exist in bank");
        }

        if ("buy".equalsIgnoreCase(type)) {

            boolean success = bankService.decrease(stockName);

            if (!success) {
                return ResponseEntity.badRequest().body("No stock available in bank");
            }

            walletService.increase(walletId, stockName);

            auditService.log("buy", walletId, stockName);

            return ResponseEntity.ok().build();
        }

        if ("sell".equalsIgnoreCase(type)) {

            boolean hasStock = walletService.decrease(walletId, stockName);

            if (!hasStock) {
                return ResponseEntity.badRequest().body("No stock in wallet");
            }

            bankService.increase(stockName);

            auditService.log("sell", walletId, stockName);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Invalid type");
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getWallet(@PathVariable String walletId) {

        return ResponseEntity.ok(
                Map.of(
                        "id", walletId,
                        "stocks", walletService.getWallet(walletId)
                )
        );
    }


    @GetMapping("/{walletId}/stocks/{stockName}")
    public int getStock(@PathVariable String walletId,
                        @PathVariable String stockName) {

        return walletService.getStock(walletId, stockName);
    }
}
