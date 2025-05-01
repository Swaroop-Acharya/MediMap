package com.medimap.order.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryClientFallback implements InventoryClient {
    @Override
    public boolean isInStock(String skuCode,Integer quantity) {
        return false; // fallback logic
    } 
}

