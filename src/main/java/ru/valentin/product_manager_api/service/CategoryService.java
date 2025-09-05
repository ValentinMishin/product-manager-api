package ru.valentin.product_manager_api.service;

import org.springframework.stereotype.Service;
import ru.valentin.product_manager_api.dto.ResponseCategoryDto;
import ru.valentin.product_manager_api.dto.mapper.DtoMapper;
import ru.valentin.product_manager_api.model.Category;
import ru.valentin.product_manager_api.repository.CategoryRepository;
import ru.valentin.product_manager_api.repository.ProductRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final DtoMapper dtoMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           DtoMapper dtoMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.dtoMapper = dtoMapper;
    }

    public List<ResponseCategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(dtoMapper::categoryToCategoryDto).toList();
    }
}
