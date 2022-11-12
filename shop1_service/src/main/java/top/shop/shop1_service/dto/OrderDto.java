package top.shop.shop1_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    private String shopServiceName;
    private String productName;
    private String customerName;
    private LocalDateTime orderDate;
    private int amount;
    private double price;
}
