package top.shop.shop1_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.shop.shop1_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByServiceName(String serviceName);
    Category findByServiceName(String serviceName);
}
