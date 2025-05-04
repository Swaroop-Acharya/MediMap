package com.medimap.order.controller;

import com.medimap.order.dto.OrderRequest;
import com.medimap.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        boolean isSuccess = orderService.placeOrder(orderRequest);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Order Placed Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }
    }
}
