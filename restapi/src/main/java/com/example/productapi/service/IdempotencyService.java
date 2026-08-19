package com.example.productapi.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores processed idempotency keys and their corresponding responses.
 */
@Service
public class IdempotencyService {

    private final Map<String, Object> responses =
            new ConcurrentHashMap<>();

    public boolean contains(String key) {
        return responses.containsKey(key);
    }

    public Object getResponse(String key) {
        return responses.get(key);
    }

    public void saveResponse(String key, Object response) {
        responses.put(key, response);
    }
}
