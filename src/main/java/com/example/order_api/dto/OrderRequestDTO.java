package com.example.order_api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public class OrderRequestDTO {

    @NotBlank(message = "customerId is required")
    private String customerId;

    @NotEmpty(message = "items must not be empty")
    private List<@Valid OrderItemDTO> items;

    public OrderRequestDTO() {}

    public OrderRequestDTO(String customerId, List<OrderItemDTO> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}