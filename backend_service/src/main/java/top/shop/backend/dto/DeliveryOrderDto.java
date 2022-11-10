package top.shop.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryOrderDto {

    private String shopServiceName;
    private String shopName;
    private String productName;
    private String customerName;
    private int amount;
    private double totalPrice;

}
