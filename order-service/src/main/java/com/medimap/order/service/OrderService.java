package com.medimap.order.service;

import com.medimap.order.client.InventoryClient;
import com.medimap.order.dto.OrderRequest;
import com.medimap.order.event.OrderPlacedEvent;
import com.medimap.order.model.Order;
import com.medimap.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public boolean placeOrder(OrderRequest orderRequest) {
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            Order order = Order.builder()
                    .orderNumber(orderRequest.orderNumber())
                    .skuCode(orderRequest.skuCode())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();
            orderRepository.save(order);
            // Send the message to the Kafka topic

            log.info("Start : Sending order placed event to Kafka topic");

            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(orderRequest.orderNumber());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            orderPlacedEvent.setPhone(orderRequest.userDetails().phoneNumber());
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End: Order placed event sent to Kafka topic");

            return true;
        }
        return false;
    }
}
