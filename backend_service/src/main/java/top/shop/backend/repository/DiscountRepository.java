package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.backend.entity.discount.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
