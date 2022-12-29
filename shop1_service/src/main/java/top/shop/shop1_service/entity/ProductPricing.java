package top.shop.shop1_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prices")
public class ProductPricing {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String productServiceName;
    private double price;

}
