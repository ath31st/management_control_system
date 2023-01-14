package top.shop.shop1_service.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.shop1_service.entity.discount.Discount;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query(value = "SELECT * FROM discounts WHERE discount_type = 0", nativeQuery = true)
    List<Discount> findAllDiscount();

    @Query(value = "SELECT * FROM discounts WHERE discount_type = 0 AND is_active = true", nativeQuery = true)
    List<Discount> findAllActiveDiscount();

    @Query("select (count(d) > 0) from Discount d where d.product.serviceName = ?1")
    boolean existsByProduct_ServiceName(String serviceName);

    Discount getByProduct_ServiceName(String serviceName);

    @Query(value = "select d from Discount as d where d.product.serviceName =?1 and type(d) = Discount")
    Discount getDiscountByProductServiceName(String serviceName);
}
