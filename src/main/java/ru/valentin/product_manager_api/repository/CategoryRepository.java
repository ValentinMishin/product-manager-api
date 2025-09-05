package ru.valentin.product_manager_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valentin.product_manager_api.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTitle(String title);
}
