package top.shop.backend.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.backend.entity.discount.CommonDiscount;

public interface CommonDiscountRepository extends JpaRepository<CommonDiscount, Long> {
    CommonDiscount findByProduct_ServiceNameAndShop_ServiceName(String serviceName, String serviceName1);
}
