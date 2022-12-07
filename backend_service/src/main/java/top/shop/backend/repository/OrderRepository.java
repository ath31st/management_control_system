package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.shop.backend.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
