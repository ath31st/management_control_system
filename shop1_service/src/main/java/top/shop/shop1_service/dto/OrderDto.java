package top.shop.shop1_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderDto {
    private String shopName;
    private String productName;
    private String customerName;
    private LocalDateTime orderDate;
}
