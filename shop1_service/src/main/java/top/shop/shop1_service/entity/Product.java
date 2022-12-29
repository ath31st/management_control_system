package top.shop.shop1_service.entity;

import lombok.*;
import top.shop.shop1_service.entity.discount.CommonDiscount;
import top.shop.shop1_service.entity.discount.Discount;
import top.shop.shop1_service.entity.discount.PrivateDiscount;

import javax.persistence.*;
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
    private String name;
    @Column(unique = true)
    private String serviceName;
    private String description;
    private long amount;
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogue_id", nullable = false)
    @ToString.Exclude
    private Catalogue catalogue;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "productPricing_id")
    private ProductPricing productPricing;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commonDiscount_id")
    private CommonDiscount commonDiscount;
    @ManyToMany
    @JoinTable(
            name = "product_privateDiscount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "privateDiscount_id"))
    @ToString.Exclude
    private Set<PrivateDiscount> privateDiscounts;
    @ManyToMany
    @JoinTable(
            name = "product_deliveryOrder",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "deliveryOrder_id"))
    @ToString.Exclude
    private Set<DeliveryOrder> deliveryOrders;
}
