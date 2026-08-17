package com.example.order_service.client;

import com.example.order_service.dto.ProductResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public ProductResponse getProduct(Long productId) {

        return restClient
                .get()
                .uri("http://product-service/products/{id}", productId)
                .retrieve()
                .body(ProductResponse.class);
    }
}