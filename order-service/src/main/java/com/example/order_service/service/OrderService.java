package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final ProductClient productClient;

    public OrderService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        ProductResponse product =
                productClient.getProduct(request.productId());

        BigDecimal totalPrice =
                product.price()
                        .multiply(BigDecimal.valueOf(request.quantity()));

        return new OrderResponse(
                1001L,
                product.id(),
                product.name(),
                request.quantity(),
                totalPrice
        );
    }
}
