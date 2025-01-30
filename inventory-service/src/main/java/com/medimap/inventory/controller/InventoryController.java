package com.medimap.inventory.controller;

import com.medimap.inventory.dto.InventoryRequest;
import com.medimap.inventory.dto.InventoryResponse;
import com.medimap.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addInventory(@RequestBody InventoryRequest inventoryRequest){
        return inventoryService.addInventory(inventoryRequest);
    }
}
