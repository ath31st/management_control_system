package top.shop.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;
    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    private Set<Catalogue> catalogs;

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
}
