package com.example.order_api.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_api.config.AppConfigProperties;
import com.example.order_api.dto.OrderRequestDTO;
import com.example.order_api.dto.OrderResponseDTO;
import com.example.order_api.service.OrderService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody @Valid OrderRequestDTO req) {
        String orderId = orderService.createOrder(req);

        Map<String, String> body = new HashMap<>();
        body.put("orderId", orderId);

        return ResponseEntity
                .created(URI.create("/orders/" + orderId))
                .body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderByIdOrThrow(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/config/app-name")
    public Map<String, String> appName(AppConfigProperties props) {
        return Map.of("appName", props.name());
    }
}