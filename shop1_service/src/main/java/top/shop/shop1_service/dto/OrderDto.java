package top.shop.shop1_service.dto;

import lombok.*;
import top.shop.shop1_service.dto.payment.PaymentDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    private String shopServiceName;
    @NotEmpty(message = "Product service name cannot be empty.")
    @Size(min = 3, max = 100, message = "Product service name must be minimum 3 and maximum 100 characters.")
    private String productServiceName;
    @NotEmpty(message = "Customer email cannot be empty.")
    @Size(min = 3, max = 100, message = "Customer email must be minimum 3 and maximum 100 characters.")
    private String customerEmail;
//    @NotBlank(message = "Promo code string cannot be blank")
    @Size(max = 20, message = "Promo code string must be maximum 20 characters.")
    private String promoCode;
    private LocalDateTime orderDate;
    @Min(value = 1, message = "Amount must be minimum 1")
    @Max(value = 100000, message = "Amount must be maximum 100000")
    private int amount;
    private PaymentDto paymentDto;
}
