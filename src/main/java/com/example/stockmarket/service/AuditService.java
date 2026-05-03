package com.example.stockmarket.service;

import com.example.stockmarket.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuditService {

    private static final String KEY = "audit_log";
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public AuditService(StringRedisTemplate redis, ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    public void log(String type, String wallet, String stock) {
        try {
            String json = mapper.writeValueAsString(
                new LogEntry(type, wallet, stock)
            );
            redis.opsForList().rightPush(KEY, json);
        } catch (Exception ignored) {}
    }

    public Map<String, Object> getLogs() {
        List<String> raw = redis.opsForList().range(KEY, 0, -1);

        List<LogEntry> logs = new ArrayList<>();

        if (raw != null) {
            for (String r : raw) {
                try {
                    logs.add(mapper.readValue(r, LogEntry.class));
                } catch (Exception ignored) {}
            }
        }

        return Map.of("log", logs);
    }
}