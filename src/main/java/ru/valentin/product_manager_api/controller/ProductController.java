package ru.valentin.product_manager_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Operation(
            summary = "Импорт товаров из внешнего API",
            description = "Импорт товаров из внешнего API (fakestoreapi.com) с сохранением в БД."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Импорт выполнен",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Ошибка при импорте товаров")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        try {
            productService.importProducts();
            return ResponseEntity.ok("Импорт товаров выполнен");
        } catch (Exception e) {
            log.error("Ошибка при импорте товаров: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте товаров: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Создание нового товара",
            description = "Возвращает созданный товар с ID и заголовком Location."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Товар создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные товара")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Parameter(description = "DTO для создания товара", required = true)
                                                                @Valid @RequestBody ProductDto productDto) {
        var createdProduct = productService.createProduct(productDto);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdProduct);
    }

    @Operation(
            summary = "Обновление товара",
            description = "Обновляются только указанные поля. Необходимо передать хотя бы одно поле"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные для обновления"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ResponseProductDto> updateProduct(
            @Parameter(description = "ID товара для обновления", required = true, example = "1")
            @PathVariable Long productId,
            @Parameter(description = "DTO для обновления товара", required = true)
            @Valid @RequestBody UpdateProductDto updateProductDto) {
        var updatedProduct = productService.updateProduct(productId, updateProductDto);

        return ResponseEntity.ok().body(updatedProduct);
    }

    @Operation(
            summary = "Удаление товара",
            description = "По указанному ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Товар удален"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара для удаления", required = true, example = "1")
            @PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получение товара по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProduct(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @Operation(
            summary = "Получение списка товаров",
            description = "С поддержкой пагинации."
    )
    @ApiResponse(responseCode = "200", description = "Список товаров получен",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)))
    @GetMapping
    public ResponseEntity<Page<ResponseProductDto>> getAllProducts(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @Operation(
            summary = "Фильтрация товаров по цене",
            description = "Возвращает товары в указанном диапазоне цен, с поддержкой пагинации."
    )
    @ApiResponse(responseCode = "200", description = "Товары отфильтрованы по цене",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)))
    @GetMapping("/filter/price")
    public ResponseEntity<Page<ResponseProductDto>> getProductsByPriceRange(
            @Parameter(description = "Минимальная цена", example = "10.0")
            @RequestParam() BigDecimal minPrice,

            @Parameter(description = "Максимальная цена", example = "100.0")
            @RequestParam() BigDecimal maxPrice,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
            ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok()
                .body(productService
                        .getProductsByPriceRange(minPrice, maxPrice, pageable));
    }

    @Operation(
            summary = "Получение товаров по категории",
            description = "Возвращает товары по ID категории, с поддержкой пагинации"
    )
    @ApiResponse(responseCode = "200", description = "Товары категории получены")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/category/title/{categoryId}")
    public ResponseEntity<Page<ResponseProductDto>> getProductsByCategoryId(
            @Parameter(description = "ID категории", required = true, example = "1")
            @PathVariable Long categoryId,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size)
    {

        Pageable pageable = PageRequest.of(page, size);

        Page<ResponseProductDto> products = productService.getProductsByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Получение товаров по категории",
            description = "Возвращает товары по названию категории, с поддержкой пагинации"
    )
    @ApiResponse(responseCode = "200", description = "Товары категории получены")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/category/title/{categoryTitle}")
    public ResponseEntity<Page<ResponseProductDto>> getProductsByCategoryTitle(
            @Parameter(description = "Название категории", required = true, example = "Хлебобулочные изделия")
            @PathVariable String categoryTitle,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size)
    {

        Pageable pageable = PageRequest.of(page, size);

        Page<ResponseProductDto> products = productService.getProductsByCategoryTitle(categoryTitle, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Получение товаров с сортировкой по цене и названию категории",
            description = "Возвращает товары с возможностью сортировки товаров сразу по двум полям (цена и название категории). С\n" +
                    " указанием отдельного направления для каждого из этих полей (возрастание/убывание)." +
                    " Можно сортировать как отдельно по каждому из этих полей, так и сразу по обоим полям"
    )
    @ApiResponse(responseCode = "200", description = "Товары получены")
    @GetMapping("/complex-sort")
    public ResponseEntity<Page<ResponseProductDto>> getAllProductsWithAdvancedSorting(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Направление сортировки по цене (asc/desc)", example = "asc")
            @RequestParam(required = false) String priceSort,

            @Parameter(description = "Направление сортировки по категории (asc/desc)", example = "asc")
            @RequestParam(required = false) String categorySort) {

        // Базовый pageable для пагинации
        Pageable pageable = PageRequest.of(page, size);

        Page<ResponseProductDto> products = productService.getAllProductsWithPriceCategoryTitleSorting(
                pageable, priceSort, categorySort);

        return ResponseEntity.ok(products);
    }
}