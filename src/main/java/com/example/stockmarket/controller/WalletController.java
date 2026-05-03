package com.example.stockmarket.controller;

import com.example.stockmarket.service.*;
import com.example.stockmarket.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final TradeService tradeService;
    private final WalletService walletService;

    public WalletController(TradeService tradeService,
                            WalletService walletService) {
        this.tradeService = tradeService;
        this.walletService = walletService;
    }

    @PostMapping("/{walletId}/stocks/{stock}")
    public ResponseEntity<?> trade(
            @PathVariable String walletId,
            @PathVariable String stock,
            @RequestBody @Valid TradeRequest request
    ) {
        tradeService.trade(walletId, stock, request.type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable String walletId) {
        return ResponseEntity.ok(
                new WalletResponse(walletId, walletService.getWallet(walletId))
        );
    }

    @GetMapping("/{walletId}/stocks/{stock}")
    public int getStock(@PathVariable String walletId,
                        @PathVariable String stock) {
        return walletService.getStock(walletId, stock);
    }
}
