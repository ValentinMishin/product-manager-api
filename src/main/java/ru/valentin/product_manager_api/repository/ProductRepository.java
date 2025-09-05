package ru.valentin.product_manager_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valentin.product_manager_api.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByExternalId(Long externalId);
    boolean existsByExternalId(Long externalId);
}
