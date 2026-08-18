package com.example.productapi.exception;


/**
 * Thrown when a requested product does not exist.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
