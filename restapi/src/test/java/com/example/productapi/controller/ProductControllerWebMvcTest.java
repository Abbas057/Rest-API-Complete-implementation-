package com.example.productapi.controller;


import com.example.productapi.dto.ProductResponse;
import com.example.productapi.exception.ProductNotFoundException;
import com.example.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productapi.dto.ProductRequest;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ProductController.class)
class ProductControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldGetProductById() throws Exception {

        ProductResponse product =
                new ProductResponse(
                        1L,
                        "Monitor",
                        25000.0,
                        "Electronics"
                );

        when(productService.getProductById(1L))
                .thenReturn(product);

        mockMvc.perform(
                        get("/api/v1/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monitor"))
                .andExpect(jsonPath("$.price").value(25000.0))
                .andExpect(jsonPath("$.category").value("Electronics"));
    }

    @Test
    void shouldCreateProduct() throws Exception {

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Monitor",
                        25000.0,
                        "Electronics"
                );

        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(response);

        String requestBody = """
            {
                "name": "Monitor",
                "price": 25000,
                "category": "Electronics"
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monitor"));
    }

    @Test
    void shouldRejectInvalidProduct() throws Exception {

        String requestBody = """
            {
                "name": "",
                "price": -100
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {

        when(productService.getProductById(999L))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(
                        get("/api/v1/products/999")
                )
                .andExpect(status().isNotFound());
    }
}