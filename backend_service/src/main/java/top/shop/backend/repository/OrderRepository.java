package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.shop.backend.entity.Order;
import top.shop.backend.util.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPayment_PaymentUuid(String paymentUuid);

    List<Order> findByStatus(OrderStatus status);

}
