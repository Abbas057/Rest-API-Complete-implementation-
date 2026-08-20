package com.example.productapi.service;

import com.example.productapi.client.InventoryClient;
import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.exception.ProductNotFoundException;
import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handles product-related business operations.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryClient inventoryClient;

    public ProductService(ProductRepository productRepository, InventoryClient inventoryClient) {
        this.productRepository = productRepository;
        this.inventoryClient = inventoryClient;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<ProductResponse> getAllProducts(
            String category,
            Pageable pageable) {

        Page<Product> products;

        if (category != null && !category.isBlank()) {
            products = productRepository.findByCategory(
                    category,
                    pageable
            );
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::toResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProductResponse getProductById(Long id) {

        return productRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(ProductRequest request) {

        Product product = toEntity(request);

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product existingProduct =
                productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setCategory(request.getCategory());

        Product savedProduct =
                productRepository.save(existingProduct);

        return toResponse(savedProduct);
    }
    public ProductResponse patchProduct(
            Long id,
            ProductRequest request) {

        Product product =
                productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }

        Product savedProduct =
                productRepository.save(product);

        return toResponse(savedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            return false;
        }

        productRepository.deleteById(id);
        return true;
    }

    private Product toEntity(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        return product;
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory()
        );
    }

    public CompletableFuture<String> getInventory(Long productId) {

        return inventoryClient.getInventory(productId);
    }
}