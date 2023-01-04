package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.shop1_service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    Customer findByEmail(String email);
    @Query(value = "select c.email from Customer c")
    String[] customersEmail();
}
