package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.Catalogue;

import java.util.Optional;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {
    Optional<Catalogue> findCatalogueByShopServiceName(String shopServiceName);

}
