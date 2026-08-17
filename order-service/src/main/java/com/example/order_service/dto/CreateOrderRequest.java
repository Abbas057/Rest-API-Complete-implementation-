package com.example.order_service.dto;

public record CreateOrderRequest(
        Long productId,
        Integer quantity
) {
}