package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
