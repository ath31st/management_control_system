package top.shop.shop1_service.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(min = 1, max = 100)
    private String name;
    @Column(unique = true)
    private String serviceName;
    private String description;
    private double price;
    private long amount;
    private String category;
    private Catalogue catalogue;
    private ProductPricing productPricing;
}
