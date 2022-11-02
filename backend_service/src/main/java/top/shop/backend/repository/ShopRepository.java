package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.shop.backend.entity.Shop;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("select s from Shop s where s.name = :shopName")
    Optional<Shop> getShop(String shopName);
}
