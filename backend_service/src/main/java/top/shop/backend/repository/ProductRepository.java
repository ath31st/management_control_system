package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.shop.backend.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("update Product p set p.amount = ?1 where p.serviceName = ?2")
    void updateAmountByServiceName(long amount, String serviceName);

    @Query("select p from Product p where p.serviceName = :serviceName")
    Optional<Product> getProduct(String serviceName);

    List<Product> getProductByCategory_ServiceName(String categoryServiceName);

    boolean existsByServiceName(String serviceName);

    @Query("select p.amount from Product as p where p.serviceName = ?1")
    long getAmountByServiceName(String serviceName);
}
