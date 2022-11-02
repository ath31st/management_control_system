package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.backend.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
