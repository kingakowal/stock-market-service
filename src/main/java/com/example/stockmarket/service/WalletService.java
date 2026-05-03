package com.example.stockmarket.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.*;

@Service
public class WalletService {
    private final StringRedisTemplate redis;

    public WalletService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String walletKey(String walletId) {
        return "wallet:" + walletId;
    }

    public Map<String, Integer> getWallet(String walletId) {
        Map<Object, Object> raw = redis.opsForHash().entries(walletKey(walletId));

        Map<String, Integer> result = new HashMap<>();
        raw.forEach((k, v) -> result.put(k.toString(), Integer.parseInt(v.toString())));

        return result;
    }

    public int getStock(String walletId, String stock) {
        Object val = redis.opsForHash().get(walletKey(walletId), stock);
        return val == null ? 0 : Integer.parseInt(val.toString());
    }


}