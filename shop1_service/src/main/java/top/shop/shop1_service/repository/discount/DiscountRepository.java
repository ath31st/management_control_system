package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.discount.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
