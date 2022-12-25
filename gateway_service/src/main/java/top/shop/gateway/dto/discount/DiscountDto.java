package top.shop.gateway.dto.discount;

import java.time.LocalDateTime;

public class DiscountDto {
    private String productServiceName;
    private String productName;
    private String shopServiceName;
    private String shopName;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private float percentageDiscount;
    private boolean isActive;
}
