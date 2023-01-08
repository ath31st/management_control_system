package top.shop.gateway.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DiscountDto {
    private String productServiceName;
    private String productName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startingDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endingDate;
    @Min(value = 1, message = "Discount cannot be lower 1%")
    @Max(value = 99, message = "Discount cannot be higher 99%")
    private float percentageDiscount;
    private boolean isActive;
}
