package com.example.inventory_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    @GetMapping("/{productId}")
    public String getInventory(@PathVariable Long productId) throws InterruptedException {

   //     Thread.sleep(5000);

        return "Inventory available for product: " + productId;
    }
}