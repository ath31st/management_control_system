package top.shop.shop1_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.shop.shop1_service.entity.ProductPricing;

@Repository
public interface ProductPricingRepository extends MongoRepository<ProductPricing, String> {
}
