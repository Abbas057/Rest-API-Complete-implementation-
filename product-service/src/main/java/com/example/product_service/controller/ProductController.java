package com.example.product_service.controller;

import com.example.product_service.dto.ProductResponse;
import com.example.product_service.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(
            @PathVariable Long id,
            @RequestHeader(
                    value = "X-Correlation-ID",
                    required = false
            )
            String correlationId){

        System.out.println(
                "Correlation ID: " + correlationId
        );


        return productService.getProduct(id);
    }
}
