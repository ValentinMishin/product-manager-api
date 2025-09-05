package ru.valentin.product_manager_api.dto.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.valentin.product_manager_api.dto.*;
import ru.valentin.product_manager_api.model.Category;
import ru.valentin.product_manager_api.model.Product;
import ru.valentin.product_manager_api.model.Rating;

public interface DtoMapper {
    //внешние
    @Mapping(source = "id", target = "external_id")
    @Mapping(source = "category", target = "category.title")
    @Mapping(target = "id", ignore = true)
    Product importProductDtoToProduct(ImportProductDto dto);

//    @Mapping(source = "external_id", target = "id")
//    @Mapping(source = "category.title", target = "category")
//    ImportProductDto productToImportProductDto(Product product);

    //локальные
    @Mapping(source = "category", target = "category.title")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "external_id", ignore = true)
    Product productDtoToProduct(ProductDto dto);

    @Mapping(source = "category.title", target = "category")
    @Mapping(source = "external_id", target = "external_id")
    ResponseProductDto productToResponseProductDto(Product product);

    RatingDto ratingToRatingDto(Rating rating);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Rating ratingDtoToRating(RatingDto ratingDto);

    ResponseCategoryDto categoryToCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Rating updateRatingDtoToRating(UpdateRatingDto dto);

    void updateRatingFromDto(UpdateRatingDto dto, @MappingTarget Rating rating);
}