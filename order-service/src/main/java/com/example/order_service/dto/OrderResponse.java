package com.example.order_service.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal totalPrice
) {
}
