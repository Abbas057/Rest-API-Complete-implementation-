package com.example.productapi.controller;

import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.service.IdempotencyService;
import com.example.productapi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;


/**
 * REST controller responsible for handling product-related HTTP requests.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    private final ProductService productService;

    private final IdempotencyService idempotencyService;

    public ProductController(ProductService productService, IdempotencyService idempotencyService) {
        this.productService = productService;
        this.idempotencyService = idempotencyService;
    }

    /**
     * Returns a simple response to verify that the product API is running.
     *
     * @return a welcome message
     */
    @GetMapping()
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) String category,
            Pageable pageable) {

        return productService.getAllProducts(category, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody ProductRequest request) {

        if (idempotencyService.contains(idempotencyKey)) {

            ProductResponse existingResponse =
                    (ProductResponse)
                            idempotencyService.getResponse(idempotencyKey);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(existingResponse);
        }

        ProductResponse response =
                productService.createProduct(request);

        idempotencyService.saveResponse(
                idempotencyKey,
                response
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateProduct(id, request);

        if (response == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.patchProduct(id, request);

        if (response == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param id identifier of the product to delete
     * @return HTTP 204 when the product is successfully deleted
     * @throws ResponseStatusException with HTTP 404 when the product does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        boolean deleted = productService.deleteProduct(id);

        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Checks whether a product exists without returning the product body.
     *
     * @param id identifier of the product
     * @return HTTP 200 if the product exists
     * @throws ResponseStatusException with HTTP 404 if the product does not exist
     */
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.HEAD
    )
    public ResponseEntity<Void> headProduct(@PathVariable Long id) {

        ProductResponse product = productService.getProductById(id);

        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Returns the HTTP methods supported by the products collection.
     *
     * @return HTTP 200 with an Allow header describing supported methods
     */
    @RequestMapping(
            value = "",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity<Void> optionsProducts() {

        return ResponseEntity
                .ok()
                .allow(
                        HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.OPTIONS
                )
                .build();
    }

    /*
    Calls Inventory service through REST
     */

    @GetMapping("/{id}/inventory")
    public CompletableFuture<String> getInventory(@PathVariable Long id) {

        return productService.getInventory(id);
    }
}