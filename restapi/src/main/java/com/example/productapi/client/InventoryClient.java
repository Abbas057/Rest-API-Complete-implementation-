package com.example.productapi.client;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;



import java.util.concurrent.CompletableFuture;

@Component
public class InventoryClient {

    private final WebClient webClient;

    public InventoryClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @CircuitBreaker(
            name = "inventory",
            fallbackMethod = "inventoryFallback"
    )
    @Retry(name = "inventory")
    @TimeLimiter(
            name = "inventory",
            fallbackMethod = "inventoryFallback"
    )
    @Bulkhead(
            name = "inventory",
            type = Bulkhead.Type.SEMAPHORE
    )
    @RateLimiter(name = "inventory")
    public CompletableFuture<String> getInventory(Long productId) {

        System.out.println(">>> Calling Inventory Service");

        return webClient
                .get()
                .uri("/api/v1/inventory/{id}", productId)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    private CompletableFuture<String> inventoryFallback(
            Long productId,
            Throwable throwable) {

        System.out.println(
                ">>> FALLBACK: "
                        + throwable.getClass().getSimpleName()
        );

        return CompletableFuture.completedFuture(
                "Inventory service is currently unavailable"
        );
    }
}
