package top.shop.backend.dto;

import lombok.Data;

@Data
public class DeliveryOrderDto {

    private String shopName;
    private String productName;
    private String customerName;
    private int amount;
    private double totalPrice;

}
