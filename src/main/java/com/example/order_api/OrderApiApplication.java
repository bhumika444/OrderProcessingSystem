package com.example.order_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.order_api.config.AppConfigProperties;

@EnableConfigurationProperties(AppConfigProperties.class)
@SpringBootApplication
public class OrderApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApiApplication.class, args);
    }
}