package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.shop.shop1_service.entity.ProductPricing;

import java.util.Optional;

@Repository
public interface ProductPricingRepository extends JpaRepository<ProductPricing, Long> {
    boolean existsByProduct_ServiceName(String serviceName);

    Optional<ProductPricing> findByProduct_ServiceName(String serviceName);
    @Query(value = "SELECT price FROM prices WHERE  = :productServiceName", nativeQuery = true)
    double getProductPrice(String productServiceName);

}
