package com.example.productapi.dto;

/**
 * Represents the product data returned by the REST API.
 */
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private String category;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String name,
            Double price,
            String category) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}