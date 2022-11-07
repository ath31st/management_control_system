package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.shop.shop1_service.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name = :name")
    Optional<Product> getProduct(String name);

    @Query("select p from Product p where p.price > 0")
    List<Product> getProductWithPrice();

    boolean existsByName(String name);

}
