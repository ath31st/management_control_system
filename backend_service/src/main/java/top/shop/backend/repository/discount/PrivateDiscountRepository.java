package top.shop.backend.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.backend.entity.discount.PrivateDiscount;

public interface PrivateDiscountRepository extends JpaRepository<PrivateDiscount, Long> {
}
