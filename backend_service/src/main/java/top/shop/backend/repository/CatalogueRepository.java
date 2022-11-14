package top.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.shop.backend.entity.Catalogue;

import java.util.Optional;

@Repository
public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {
    Optional<Catalogue> findByShop_ServiceName(String serviceName);
    boolean existsByShop_ServiceName(String serviceName);

}
