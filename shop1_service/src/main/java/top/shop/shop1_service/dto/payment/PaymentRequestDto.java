package top.shop.shop1_service.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestDto {
    @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}", message = "Please, enter your UUID payment")
    private String paymentUuid;
    @Min(value = 0, message = "Price must be minimum 0")
    @Max(value = 1000000, message = "Price must be maximum 1000000")
    private BigDecimal totalPrice;
}
