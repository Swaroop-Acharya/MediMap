package com.medimap.order.service;

import com.medimap.order.dto.OrderRequest;
import com.medimap.order.model.Order;
import com.medimap.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(orderRequest.orderNumber())
                .skuCode(orderRequest.skuCode())
                .price(orderRequest.price())
                .quantity(orderRequest.quantity())
                .build();
        orderRepository.save(order);

    }
}
