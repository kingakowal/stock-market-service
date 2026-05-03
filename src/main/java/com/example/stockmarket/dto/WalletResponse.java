package com.example.stockmarket.dto;

import java.util.Map;

public record WalletResponse(
        String id,
        Map<String, Integer> stocks
) {}