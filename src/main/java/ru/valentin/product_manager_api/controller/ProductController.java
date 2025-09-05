package ru.valentin.product_manager_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valentin.product_manager_api.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productImportService;

    public ProductController(ProductService productImportService) {
        this.productImportService = productImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        try {
            productImportService.importProducts();
            return ResponseEntity.ok("Импорт товаров запущен");
        } catch (Exception e) {
            log.error("Ошибка при импорте товаров: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте товаров: " + e.getMessage());
        }
    }


}
