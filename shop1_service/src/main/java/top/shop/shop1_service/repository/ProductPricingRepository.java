package top.shop.shop1_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import top.shop.shop1_service.entity.ProductPricing;

import java.util.Optional;

@Repository
public interface ProductPricingRepository extends MongoRepository<ProductPricing, String> {
    @Query("{productServiceName:'?0'}")
    Optional<ProductPricing> findByProductServiceName(String productServiceName);

    boolean existsByProductServiceName(String productServiceName);

}
