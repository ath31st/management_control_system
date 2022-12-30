package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.DeliveryOrder;

import java.util.Optional;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    Optional<DeliveryOrder> findByOrderUuidNumber(String orderUuidNumber);

    boolean existsByOrderUuidNumber(String orderUuidNumber);

}
