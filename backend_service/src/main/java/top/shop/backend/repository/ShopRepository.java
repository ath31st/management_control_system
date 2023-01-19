package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.shop.backend.entity.Shop;

import java.math.BigDecimal;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Transactional
    @Modifying
    @Query("update Shop s set s.balance = ?1 where s.serviceName = ?2")
    void updateBalanceByServiceName(BigDecimal balance, String serviceName);
    @Query("select s from Shop s where s.serviceName = :serviceName")
    Optional<Shop> getShop(String serviceName);
    @Query("select s.balance from Shop as s where s.serviceName =?1")
    BigDecimal getBalance(String serviceName);

    boolean existsByServiceName(String serviceName);
}
