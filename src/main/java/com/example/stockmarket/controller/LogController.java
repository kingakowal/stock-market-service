package com.example.stockmarket.controller;

import com.example.stockmarket.service.AuditService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class LogController {

    private final AuditService auditService;

    public LogController(AuditService a) {
        this.auditService = a;
    }

    @GetMapping
    public Object getLog() {
        return auditService.getLogs();
    }
}
