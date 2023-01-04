package top.shop.shop1_service.entity;

import lombok.*;
import top.shop.shop1_service.entity.discount.CommonDiscount;
import top.shop.shop1_service.entity.discount.Discount;
import top.shop.shop1_service.entity.discount.PrivateDiscount;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogue_id")
    @ToString.Exclude
    private Catalogue catalogue;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "productPricing_id")
    private ProductPricing productPricing;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commonDiscount_id")
    private CommonDiscount commonDiscount;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "privateDiscount_id")
    private PrivateDiscount privateDiscount;
    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    private List<DeliveryOrder> deliveryOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(serviceName, product.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
