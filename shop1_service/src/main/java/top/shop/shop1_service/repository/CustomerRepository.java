package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.shop1_service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);

    Customer findByUsername(String username);
    @Query(value = "select c.username from Customer c")
    String[] customersUsername();
}
