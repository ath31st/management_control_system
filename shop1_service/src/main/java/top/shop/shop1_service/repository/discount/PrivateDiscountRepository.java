package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.discount.PrivateDiscount;

public interface PrivateDiscountRepository extends JpaRepository<PrivateDiscount, Long> {
    PrivateDiscount getByProduct_ServiceNameAndCustomer_Email(String serviceName, String email);

    boolean existsByPromoCodeAndProduct_ServiceNameAndCustomer_Email(String promoCode, String serviceName, String email);

}
