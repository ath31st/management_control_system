package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.backend.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.serviceName = :serviceName")
    Optional<Product> getProduct(String serviceName);
}
