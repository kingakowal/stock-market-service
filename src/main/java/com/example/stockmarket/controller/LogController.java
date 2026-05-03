package com.example.stockmarket.controller;

import com.example.stockmarket.service.AuditService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class LogController {

    private final AuditService auditService;

    public LogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public Map<String, Object> getLog() {
        return auditService.getLogs();
    }
}