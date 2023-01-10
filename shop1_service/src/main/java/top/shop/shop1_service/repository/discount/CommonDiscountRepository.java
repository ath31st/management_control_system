package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.shop.shop1_service.entity.discount.CommonDiscount;

public interface CommonDiscountRepository extends JpaRepository<CommonDiscount, Long> {
    @Transactional
    @Modifying
    @Query("update CommonDiscount c set c.numberOfAvailable = ?1 where c.id = ?2")
    void updateNumberOfAvailableById(Long numberOfAvailable, Long id);
    boolean existsByPromoCodeAndProduct_ServiceName(String promoCode, String serviceName);
    CommonDiscount getByProduct_ServiceName(String serviceName);
}
