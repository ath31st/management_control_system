package top.shop.shop1_service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "catalogs")
public class Catalogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String shopServiceName;
    private LocalDateTime catalogueOnDate;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catalogue catalogue = (Catalogue) o;
        return Objects.equals(id, catalogue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Catalogue{" +
                "id=" + id +
                ", shopServiceName='" + shopServiceName + '\'' +
                ", catalogueOnDate=" + catalogueOnDate +
                '}';
    }
}
