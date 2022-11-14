package top.shop.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "catalogs")
public class Catalogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime catalogueOnDate;
    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogue")
    @ToString.Exclude
    private Set<Product> products;
}
