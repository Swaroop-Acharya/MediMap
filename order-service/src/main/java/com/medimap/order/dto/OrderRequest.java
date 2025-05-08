package com.medimap.order.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity,
        UserDetails userDetails) {
    public record UserDetails(String name, String email, String phoneNumber) {
    }
}
