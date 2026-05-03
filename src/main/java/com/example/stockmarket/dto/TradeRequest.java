package com.example.stockmarket.dto;

import jakarta.validation.constraints.NotBlank;

public class TradeRequest {

    @NotBlank
    public String type;
}