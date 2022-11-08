package top.shop.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {

    private String shopName;
    private String productName;
    private String customerName;
    private LocalDateTime orderDate;
    private int amount;
    private double price;

}
