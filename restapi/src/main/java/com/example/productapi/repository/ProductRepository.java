package com.example.productapi.repository;

import com.example.productapi.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository responsible for Product persistence operations.
 */
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(
            String category,
            Pageable pageable
    );
}