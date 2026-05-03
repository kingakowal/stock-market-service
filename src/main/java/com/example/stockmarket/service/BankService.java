package com.example.stockmarket.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BankService {
    private final StringRedisTemplate redis;

    public BankService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private static final String BANK_KEY = "bank";

    public Map<String, Integer> getAllStocks() {
        Map<Object, Object> raw = redis.opsForHash().entries(BANK_KEY);

        Map<String, Integer> result = new java.util.HashMap<>();

        raw.forEach((k, v) -> {
            result.put(
                    k.toString(),
                    Integer.parseInt(v.toString())
            );
        });

        return result;
    }

    public void setStocks(Map<String, Integer> stocks) {
        redis.delete(BANK_KEY);

        stocks.forEach((name, qty) ->
                redis.opsForHash().put(BANK_KEY, name, String.valueOf(qty))
        );
    }

    public boolean hasStock(String stock) {
        return redis.opsForHash().hasKey(BANK_KEY, stock);
    }

    public boolean decrease(String stock) {
        Object current = redis.opsForHash().get(BANK_KEY, stock);
        int value = current == null ? 0 : Integer.parseInt(current.toString());

        if (value <= 0) {
            return false;
        }

        redis.opsForHash().increment(BANK_KEY, stock, -1);
        return true;
    }

    public void increase(String stock) {
        redis.opsForHash().increment(BANK_KEY, stock, 1);
    }

    public int getStock(String stock) {
        Object val = redis.opsForHash().get(BANK_KEY, stock);
        return val == null ? 0 : Integer.parseInt(val.toString());

    }
}