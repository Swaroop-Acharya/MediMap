package com.medimap.order.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryClientFallback implements InventoryClient {
    @Override
    public boolean isInStock(String skuCode, Integer quantity) {
        log.error("Fallback called for SkuCode: "+skuCode);
        return false; // Default response on fallback
    }
}

