package top.shop.shop1_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import top.shop.shop1_service.util.DeliveryStatus;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Document("deliveries")
public class DeliveryOrder {
    @Id
    private String orderUuidNumber;
    private String shopServiceName;
    private String shopName;
    private String productName;
    private String customerName;
    private int amount;
    private double totalPrice;
    private DeliveryStatus deliveryStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryOrder that = (DeliveryOrder) o;
        return Objects.equals(orderUuidNumber, that.orderUuidNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderUuidNumber);
    }
}
