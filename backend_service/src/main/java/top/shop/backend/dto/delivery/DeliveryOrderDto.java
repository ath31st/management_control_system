package top.shop.backend.dto.delivery;

import lombok.Builder;
import lombok.Data;
import top.shop.backend.util.DeliveryStatus;

@Data
@Builder
public class DeliveryOrderDto {
    private String orderUuidNumber;
    private String shopServiceName;
    private String shopName;
    private String productName;
    private String customerEmail;
    private int amount;
    private double totalPrice;
    private DeliveryStatus deliveryStatus;
}
