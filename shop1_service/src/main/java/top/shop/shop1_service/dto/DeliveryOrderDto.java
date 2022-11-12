package top.shop.shop1_service.dto;

import lombok.Data;

@Data
public class DeliveryOrderDto {
    private String shopServiceName;
    private String shopName;
    private String productName;
    private String customerName;
    private int amount;
    private double totalPrice;
}
