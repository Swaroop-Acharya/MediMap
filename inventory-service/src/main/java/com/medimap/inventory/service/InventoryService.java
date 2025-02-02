package com.medimap.inventory.service;

import com.medimap.inventory.dto.InventoryRequest;
import com.medimap.inventory.dto.InventoryResponse;
import com.medimap.inventory.model.Inventory;
import com.medimap.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity){
        //Find an inventory for given skuCode where quantity >= 0
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode,quantity);
    }

    public InventoryResponse addInventory(InventoryRequest inventoryRequest){
        Inventory inventory= Inventory.builder()
                .skuCode(inventoryRequest.skuCode())
                .quantity(inventoryRequest.quantity())
                .build();
        inventoryRepository.save(inventory);
        return new InventoryResponse(inventory.getId(),inventory.getSkuCode(),inventory.getQuantity());
    }
}
