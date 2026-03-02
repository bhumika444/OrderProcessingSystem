package com.example.order_api.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItemDTO() {}

    public OrderItemDTO(String sku, Integer quantity, BigDecimal unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}