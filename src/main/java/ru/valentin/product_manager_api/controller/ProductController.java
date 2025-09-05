package ru.valentin.product_manager_api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.valentin.product_manager_api.dto.ProductDto;
import ru.valentin.product_manager_api.dto.ResponseProductDto;
import ru.valentin.product_manager_api.dto.UpdateProductDto;
import ru.valentin.product_manager_api.service.ProductService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        try {
            productService.importProducts();
            return ResponseEntity.ok("Импорт товаров запущен");
        } catch (Exception e) {
            log.error("Ошибка при импорте товаров: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте товаров: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        var createdProduct = productService.createProduct(productDto);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdProduct);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable Long productId,
                                                            @Valid @RequestBody UpdateProductDto updateProductDto) {
        var updatedProduct = productService.updateProduct(productId, updateProductDto);

        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @GetMapping("/filter/price")
    public ResponseEntity<Page<ResponseProductDto>> getProductsByPriceRange(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (minPrice == null) minPrice = BigDecimal.ZERO;
        if (maxPrice == null) maxPrice = new BigDecimal("999999.99");

        return ResponseEntity.ok()
                .body(productService
                        .getProductsByPriceRange(minPrice, maxPrice, pageable));
    }
}