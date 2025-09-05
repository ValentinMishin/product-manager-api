package ru.valentin.product_manager_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valentin.product_manager_api.dto.ResponseCategoryDto;
import ru.valentin.product_manager_api.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseCategoryDto>> getAllCategories() {

        return ResponseEntity.ok().body(categoryService.getAllCategories()) ;
    }
}
