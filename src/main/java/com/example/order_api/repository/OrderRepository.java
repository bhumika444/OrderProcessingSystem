package com.example.order_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order_api.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}