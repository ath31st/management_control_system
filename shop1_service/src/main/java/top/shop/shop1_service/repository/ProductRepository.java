package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.shop1_service.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByServiceName(String serviceName);

    Optional<Product> findByServiceName(String serviceName);
    @Query(value = "SELECT amount FROM products WHERE service_name = :productServiceName", nativeQuery = true)
    long getAmountProductFromCatalogue(String productServiceName);

}
