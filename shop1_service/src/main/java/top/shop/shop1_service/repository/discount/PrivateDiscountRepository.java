package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.shop.shop1_service.entity.discount.PrivateDiscount;

public interface PrivateDiscountRepository extends JpaRepository<PrivateDiscount, Long> {
    @Transactional
    @Modifying
    @Query("update PrivateDiscount p set p.isApplied = ?1 where p.id = ?2")
    void updateIsAppliedById(boolean isApplied, Long id);
    PrivateDiscount getByProduct_ServiceNameAndCustomer_Email(String serviceName, String email);

    boolean existsByPromoCodeAndProduct_ServiceNameAndCustomer_Email(String promoCode, String serviceName, String email);

}
