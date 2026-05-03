package com.example.stockmarket.service;

import com.example.stockmarket.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditService {

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String LOG_KEY = "audit_log";

    public AuditService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void log(String type, String walletId, String stock) {
        try {
            LogEntry entry = new LogEntry(type, walletId, stock);

            String json = mapper.writeValueAsString(entry);
            redis.opsForList().rightPush(LOG_KEY, json);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getLogs() {
        List<String> raw = redis.opsForList().range(LOG_KEY, 0, -1);

        List<LogEntry> parsed = new ArrayList<>();

        if (raw != null) {
            for (String r : raw) {
                try {
                    parsed.add(mapper.readValue(r, LogEntry.class));
                } catch (Exception ignored) {}
            }
        }

        return Map.of("log", parsed);
    }
}