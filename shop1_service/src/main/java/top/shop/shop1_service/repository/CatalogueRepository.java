package top.shop.shop1_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import top.shop.shop1_service.entity.Catalogue;

import java.util.Optional;

public interface CatalogueRepository extends MongoRepository<Catalogue, String> {

    @Query("{shopServiceName:'?0'}")
    Optional<Catalogue> findCatalogueByShopServiceName(String shopServiceName);

}
