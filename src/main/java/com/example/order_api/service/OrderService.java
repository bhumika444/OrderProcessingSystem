package com.example.order_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.order_api.domain.Order;
import com.example.order_api.dto.OrderItemDTO;
import com.example.order_api.dto.OrderRequestDTO;
import com.example.order_api.dto.OrderResponseDTO;
import com.example.order_api.exception.OrderNotFoundException;

@Service
public class OrderService {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public String createOrder(OrderRequestDTO req) {
        String id = UUID.randomUUID().toString();
        BigDecimal total = computeTotal(req.getItems());

        Order order = new Order(id, req.getCustomerId(), "CREATED", total);
        store.put(id, order);

        return id;
    }

    public OrderResponseDTO getOrderByIdOrThrow(String id) {
    Order order = store.get(id);
    if (order == null) throw new OrderNotFoundException(id);
    return toResponse(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<OrderResponseDTO> result = new ArrayList<>();
        for (Order order : store.values()) {
            result.add(toResponse(order));
        }
        // optional: stable ordering (nice for tests)
        result.sort(Comparator.comparing(OrderResponseDTO::getOrderId));
        return result;
    }

    private BigDecimal computeTotal(List<OrderItemDTO> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDTO item : items) {
            if (item == null) continue;

            Integer quantity = item.getQuantity();
            int qty = quantity != null ? quantity : 0;
            BigDecimal unit = item.getUnitPrice() == null ? BigDecimal.ZERO : item.getUnitPrice();

            total = total.add(unit.multiply(BigDecimal.valueOf(qty)));
        }
        return total;
    }

    private OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }
}