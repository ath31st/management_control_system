package top.shop.shop1_service.entity;

import lombok.*;
import top.shop.shop1_service.dto.product.ProductDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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
    private Set<Product> products;
}
