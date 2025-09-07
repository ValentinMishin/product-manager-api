package ru.valentin.product_manager_api.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.valentin.product_manager_api.model.Category;
import ru.valentin.product_manager_api.model.Product;
import ru.valentin.product_manager_api.model.Rating;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    //внешние
    @Mapping(source = "id", target = "externalId")
    @Mapping(source = "category", target = "category.title")
    @Mapping(target = "id", ignore = true)
    Product importProductDtoToProduct(ImportProductDto dto);

//    @Mapping(source = "external_id", target = "id")
//    @Mapping(source = "category.title", target = "category")
//    ImportProductDto productToImportProductDto(Product product);

    //локальные
    @Mapping(source = "category", target = "category.title")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    Product productDtoToProduct(ProductDto dto);

    @Mapping(source = "category.title", target = "category")
    @Mapping(source = "externalId", target = "externalId")
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