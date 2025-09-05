package ru.valentin.product_manager_api.dto.mapper;

import org.mapstruct.Mapping;
import ru.valentin.product_manager_api.dto.ImportProductDto;
import ru.valentin.product_manager_api.dto.RatingDto;
import ru.valentin.product_manager_api.model.Product;
import ru.valentin.product_manager_api.model.Rating;

public interface ProductMapper {
    @Mapping(source = "id", target = "external_id")
    @Mapping(source = "category", target = "category.title")
    @Mapping(target = "id", ignore = true)
    Product importProductDtoToProduct(ImportProductDto dto);

    @Mapping(source = "external_id", target = "id")
    @Mapping(source = "category.title", target = "category")
    ImportProductDto productToImportProductDto(Product product);

    RatingDto ratingToRatingDto(Rating rating);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Rating ratingDtoToRating(RatingDto ratingDto);
}