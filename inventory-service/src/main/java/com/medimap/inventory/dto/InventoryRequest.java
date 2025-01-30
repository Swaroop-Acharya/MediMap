package com.medimap.inventory.dto;

public record InventoryRequest(Long id, String skuCode, Integer quantity) {
}
