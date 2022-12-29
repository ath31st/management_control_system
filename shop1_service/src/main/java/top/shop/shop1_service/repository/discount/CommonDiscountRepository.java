package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.discount.CommonDiscount;

public interface CommonDiscountRepository extends JpaRepository<CommonDiscount, Long> {

}
