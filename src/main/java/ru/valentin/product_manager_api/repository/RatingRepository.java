package ru.valentin.product_manager_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valentin.product_manager_api.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> { }
