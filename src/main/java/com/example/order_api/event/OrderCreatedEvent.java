package com.example.order_api.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
    String eventId,
    Instant occurredAt,
    String orderId,
    String customerId,
    List<Item> items,
    BigDecimal totalAmount,
    int eventVersion
) {
  public record Item(String sku, int quantity, BigDecimal unitPrice) {}
}