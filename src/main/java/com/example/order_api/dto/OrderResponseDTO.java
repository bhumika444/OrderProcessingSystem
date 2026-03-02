package com.example.order_api.dto;

import java.math.BigDecimal;

public class OrderResponseDTO {
    private String orderId;
    private String customerId;
    private String status;
    private BigDecimal totalAmount;

    public OrderResponseDTO() {}

    public OrderResponseDTO(String orderId, String customerId, String status, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}