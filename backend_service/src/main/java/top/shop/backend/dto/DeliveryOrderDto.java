package top.shop.backend.dto;

import lombok.Builder;
import lombok.Data;
import top.shop.backend.util.DeliveryStatus;

@Data
@Builder
public class DeliveryOrderDto {
    private Long orderNumber;
    private String shopServiceName;
    private String shopName;
    private String productName;
    private String customerName;
    private int amount;
    private double totalPrice;
    private DeliveryStatus deliveryStatus;
}
