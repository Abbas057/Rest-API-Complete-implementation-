package com.example.product_service.service;

import com.example.product_service.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProductService {

    @Value("${server.port}")
    private String port;

    private final Map<Long, ProductResponse> products = Map.of(
            1L, new ProductResponse(
                    1L,
                    "Laptop",
                    new BigDecimal("75000")
            ),
            2L, new ProductResponse(
                    2L,
                    "Keyboard",
                    new BigDecimal("2500")
            )
    );

    public ProductResponse getProduct(Long id) {


        System.out.println(">>> Request handled by Product Service on port: "
                + port);

        ProductResponse product = products.get(id);

        if (product == null) {
            throw new RuntimeException("Product not found: " + id);
        }

        return product;
    }
}