package top.shop.shop1_service.dto.delivery;

import lombok.Data;
import top.shop.shop1_service.util.DeliveryStatus;

@Data
public class DeliveryOrderDto {
    private String orderUuidNumber;
    private String shopServiceName;
    private String shopName;
    private String productServiceName;
    private String customerEmail;
    private int amount;
    private double totalPrice;
    private DeliveryStatus deliveryStatus;
}
