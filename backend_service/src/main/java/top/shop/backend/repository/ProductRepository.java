package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
