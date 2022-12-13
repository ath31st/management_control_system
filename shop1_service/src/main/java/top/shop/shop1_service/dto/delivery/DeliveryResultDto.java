package top.shop.shop1_service.dto.delivery;

import lombok.Data;
import top.shop.shop1_service.util.DeliveryStatus;

@Data
public class DeliveryResultDto {
    DeliveryStatus deliveryStatus;
}
