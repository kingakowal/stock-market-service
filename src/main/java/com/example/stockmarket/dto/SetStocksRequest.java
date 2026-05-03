package com.example.stockmarket.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SetStocksRequest {

    public SetStocksRequest() {}

    @NotNull
    @NotEmpty
    public List<@Valid StockDto> stocks;
}