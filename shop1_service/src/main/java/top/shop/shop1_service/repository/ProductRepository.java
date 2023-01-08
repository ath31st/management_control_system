package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.shop.shop1_service.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("update Product p set p.amount = ?1 where p.serviceName = ?2")
    void updateAmountByServiceName(long amount, String serviceName);

    boolean existsByServiceName(String serviceName);

    @Query(value = "SELECT * FROM products WHERE service_name IN(:serviceNames)", nativeQuery = true)
    List<Product> getProductsByList(List<String> serviceNames);

    Optional<Product> findByServiceName(String serviceName);

    @Query(value = "SELECT amount FROM products WHERE service_name = :productServiceName", nativeQuery = true)
    long getAmountProductFromCatalogue(String productServiceName);

    @Query(value = "SELECT p.price FROM products JOIN prices p ON p.id = products.product_pricing_id WHERE service_name = :productServiceName", nativeQuery = true)
    double getPoductPrice(String productServiceName);
}
