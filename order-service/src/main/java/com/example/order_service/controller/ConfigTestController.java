package com.example.order_service.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigTestController {

    @Value("${order.max-items}")
    private int maxItems;

    @Value("${order.message}")
    private String message;

    @GetMapping
    public Map<String, Object> getConfig() {
        return Map.of(
                "maxItems", maxItems,
                "message", message
        );
    }
}
