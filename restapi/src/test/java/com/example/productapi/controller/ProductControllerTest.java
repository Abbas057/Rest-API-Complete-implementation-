package com.example.productapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAllProducts() throws Exception {

        mockMvc.perform(get("/api/v1/products")).andExpect(status().isOk());
    }

    @Test
    void shouldGetProductById() throws Exception {

        mockMvc.perform(get("/api/v1/products/1")).andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/v1/products/99999")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProduct() throws Exception {

        String requestBody = """
                {
                    "name": "Monitor",
                    "price": 25000,
                    "category": "Electronics"
                }
                """;

        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
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
    void shouldDeleteProduct() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/products/1")
                )
                .andExpect(status().isNoContent());
    }


}
