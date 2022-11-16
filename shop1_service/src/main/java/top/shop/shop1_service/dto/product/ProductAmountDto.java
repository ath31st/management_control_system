package top.shop.shop1_service.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductAmountDto {
    private long amount;
    private String productServiceName;
    private LocalDateTime onDate;
}
