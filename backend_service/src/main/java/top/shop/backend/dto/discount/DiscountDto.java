package top.shop.backend.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
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
