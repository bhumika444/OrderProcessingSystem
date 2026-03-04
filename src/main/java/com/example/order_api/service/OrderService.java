package com.example.order_api.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.order_api.dto.OrderItemDTO;
import com.example.order_api.dto.OrderRequestDTO;
import com.example.order_api.dto.OrderResponseDTO;
import com.example.order_api.entity.OrderEntity;
import com.example.order_api.entity.OrderItemEntity;
import com.example.order_api.event.OrderCreatedEvent;
import com.example.order_api.exception.OrderNotFoundException;
import com.example.order_api.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final String ordersCreatedTopic; 

    
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate, @Value("${app.kafka.topics.ordersCreated}") String ordersCreatedTopic) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.ordersCreatedTopic = "orders.created";
    }
    @Transactional
    public String createOrder(OrderRequestDTO req) {
        String id = UUID.randomUUID().toString();
        BigDecimal total = computeTotal(req.getItems());

        OrderEntity order = new OrderEntity(id, req.getCustomerId(), "CREATED", total);
        for (OrderItemDTO dto : req.getItems()) {
            OrderItemEntity item = new OrderItemEntity(dto.getSku(), dto.getQuantity(), dto.getUnitPrice());
            order.addItem(item); // sets both sides
        }
        // save parent; items saved via cascade
        orderRepository.save(order);

        OrderCreatedEvent evt = new OrderCreatedEvent(
            java.util.UUID.randomUUID().toString(),
            java.time.Instant.now(),
            order.getId(),
            order.getCustomerId(),
            order.getItems().stream()
                .map(i -> new OrderCreatedEvent.Item(i.getSku(), i.getQuantity(), i.getUnitPrice()))
                .toList(),
            order.getTotalAmount(),
            1
        );

        // publish ONLY after DB commit succeeds
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                kafkaTemplate.send(ordersCreatedTopic, order.getId(), evt); // key = orderId ✅
            }
        });

        return id;
    }

    public OrderResponseDTO getOrderByIdOrThrow(String id) {
        OrderEntity order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        return toResponse(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .sorted(Comparator.comparing(OrderResponseDTO::getOrderId))
                .toList();
        }

    private BigDecimal computeTotal(List<OrderItemDTO> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDTO item : items) {
            Integer qtyObj = item.getQuantity();
            int qty = (qtyObj == null) ? 0 : qtyObj;
            BigDecimal unit = item.getUnitPrice() == null ? BigDecimal.ZERO : item.getUnitPrice();
            total = total.add(unit.multiply(BigDecimal.valueOf(qty)));
        }
        return total;
    }

    private OrderResponseDTO toResponse(OrderEntity order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }
}