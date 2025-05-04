package com.medimap.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", fallback = InventoryClientFallback.class, url = "${service.inventory.url}")
public interface InventoryClient {

    @GetMapping("api/inventory/stock")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

}