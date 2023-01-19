package top.shop.backend.dto;

import lombok.*;
import top.shop.backend.dto.payment.PaymentDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    private String shopServiceName;
    @NotEmpty(message = "Product name cannot be empty.")
    @Size(min = 3, max = 100, message = "Product name must be minimum 3 and maximum 100 characters.")
    private String productServiceName;
    @NotEmpty(message = "Customer email cannot be empty.")
    @Size(min = 3, max = 100, message = "Customer email must be minimum 3 and maximum 100 characters.")
    private String customerEmail;
    private LocalDateTime orderDate;
    @Min(value = 1, message = "Amount must be minimum 1")
    @Max(value = 10000, message = "Amount must be maximum 10000")
    private int amount;
    private PaymentDto paymentDto;
}
