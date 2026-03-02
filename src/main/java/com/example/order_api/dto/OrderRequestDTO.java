package com.example.order_api.dto;


import java.util.List;


public class OrderRequestDTO {
    private String customerId;
    private List<OrderItemDTO> items;

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