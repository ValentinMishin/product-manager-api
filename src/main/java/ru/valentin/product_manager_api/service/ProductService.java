package ru.valentin.product_manager_api.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.valentin.product_manager_api.dto.*;
import ru.valentin.product_manager_api.dto.DtoMapper;
import ru.valentin.product_manager_api.model.Category;
import ru.valentin.product_manager_api.model.Product;
import ru.valentin.product_manager_api.model.Rating;
import ru.valentin.product_manager_api.repository.CategoryRepository;
import ru.valentin.product_manager_api.repository.ProductRepository;
import ru.valentin.product_manager_api.repository.RatingRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ExternalApiService externalApiService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;
    private final DtoMapper dtoMapper;

    public ProductService(ExternalApiService externalApiService, ProductRepository productRepository, CategoryRepository categoryRepository, RatingRepository ratingRepository, DtoMapper dtoMapper) {
        this.externalApiService = externalApiService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ratingRepository = ratingRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public ResponseProductDto createProduct(ProductDto productDto) {
        log.info("Создание нового товара: {}", productDto.getTitle());

        // Преобразование DTO в сущность
        Product product = dtoMapper.productDtoToProduct(productDto);

        // Обработка категории
        Category category = getOrCreateCategory(productDto.getCategory());
        product.setCategory(category);

        // Обработка рейтинга
        Rating rating = dtoMapper.ratingDtoToRating(productDto.getRating());
        ratingRepository.save(rating);
        product.assignRating(rating);

        // Сохранение товара
        Product savedProduct = productRepository.save(product);
        log.info("Товар создан с ID: {}", savedProduct.getId());

        return dtoMapper.productToResponseProductDto(savedProduct);
    }

    @Transactional
    public ResponseProductDto updateProduct(Long productId, UpdateProductDto updateProductDto) {
        log.info("Обновление товара с ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + productId + " не найден"));

        // Обновление полей, если они указаны
        if (updateProductDto.getTitle() != null) {
            product.setTitle(updateProductDto.getTitle());
        }
        if (updateProductDto.getPrice() != null) {
            product.setPrice(updateProductDto.getPrice());
        }
        if (updateProductDto.getDescription() != null) {
            product.setDescription(updateProductDto.getDescription());
        }

        // Обновление категории, если указана
        if (updateProductDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateProductDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + updateProductDto.getCategoryId() + " не найдена"));
            product.setCategory(category);
        }

        // Обновление рейтинга, если указан
        if (updateProductDto.getRating() != null && updateProductDto.getRating().hasAtLeastOneField()) {
            updateRating(product, updateProductDto.getRating());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Товар с ID {} обновлен", productId);

        return dtoMapper.productToResponseProductDto(updatedProduct);
    }

    @Transactional
    private void updateRating(Product product, UpdateRatingDto updateRatingDto) {
        Rating rating = product.getRating();

        if (rating == null) {
            rating = new Rating();
            product.assignRating(rating);
        }

        if (updateRatingDto.getRate() != null) {
            rating.setRate(updateRatingDto.getRate());
        }
        if (updateRatingDto.getCount() != null) {
            rating.setCount(updateRatingDto.getCount());
        }

        ratingRepository.save(rating);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Товар с ID " + productId + " не найден");
        }

        productRepository.deleteById(productId);
        log.info("Товар с ID {} удален", productId);
    }

    public ResponseProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + productId + " не найден"));

        return dtoMapper.productToResponseProductDto(product);
    }

    public Page<ResponseProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(dtoMapper::productToResponseProductDto);
    }

    public Page<ResponseProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return productPage.map(dtoMapper::productToResponseProductDto);
    }

    public Page<ResponseProductDto> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

        return productPage.map(dtoMapper::productToResponseProductDto);
    }

    public Page<ResponseProductDto> getProductsByCategoryTitle(String category, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryTitle(category, pageable);

        return productPage.map(dtoMapper::productToResponseProductDto);
    }

    public Page<ResponseProductDto> getAllProductsWithPriceCategoryTitleSorting(
            Pageable pageable,
            String priceSortDirection,
            String categorySortDirection) {

        Sort sort = createPriceCategoryTitleSort(priceSortDirection, categorySortDirection);
        Pageable priceCategoryPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return getAllProducts(priceCategoryPageable);
    }

    private Sort createPriceCategoryTitleSort(String priceSortDirection, String categorySortDirection) {
        List<Sort.Order> orders = new ArrayList<>();

        // Добавляем сортировку по цене если указано направление
        if (priceSortDirection != null && !priceSortDirection.trim().isEmpty()) {
            Sort.Direction priceDirection = Sort.Direction.fromString(priceSortDirection);
            orders.add(new Sort.Order(priceDirection, "price"));
        }

        // Добавляем сортировку по категории если указано направление
        if (categorySortDirection != null && !categorySortDirection.trim().isEmpty()) {
            Sort.Direction categoryDirection = Sort.Direction.fromString(categorySortDirection);
            orders.add(new Sort.Order(categoryDirection, "category.title"));
        }

        // Если не указано ни одного направления используем сортировку по умолчанию
        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        }

        return Sort.by(orders);
    }

    @Transactional
    public void importProducts() {
        log.info("Начало импорта товаров из внешнего API");

        List<ImportProductDto> externalProducts = externalApiService.fetchProductsFromExternalApi();

        if (externalProducts.isEmpty()) {
            log.warn("Не удалось получить товары из внешнего API");
            return;
        }

        int importedCount = 0;
        int updatedCount = 0;

        for (ImportProductDto externalProduct : externalProducts) {
            try {
                boolean isUpdated = importOrUpdateProduct(externalProduct);
                if (isUpdated) {
                    updatedCount++;
                } else {
                    importedCount++;
                }
            } catch (Exception e) {
                log.error("Ошибка при импорте товара с external_id {}: {}",
                        externalProduct.getId(), e.getMessage(), e);
            }
        }

        log.info("Импорт завершен. Добавлено: {}, Обновлено: {}", importedCount, updatedCount);
    }

    private boolean importOrUpdateProduct(ImportProductDto externalProduct) {
        Optional<Product> existingProductOpt = productRepository.findByExternalId(externalProduct.getId());

        if (existingProductOpt.isPresent()) {
            // Обновление существующего товара
            Product existingProduct = existingProductOpt.get();
            updateProductFromExternalData(existingProduct, externalProduct);
            productRepository.save(existingProduct);
            return true;
        } else {
            // Создание нового товара
            Product newProduct = createProductFromExternalData(externalProduct);
            productRepository.save(newProduct);
            return false;
        }
    }

    private Product createProductFromExternalData(ImportProductDto externalProduct) {
        Product product = dtoMapper.importProductDtoToProduct(externalProduct);

        // Обработка категории
        Category category = getOrCreateCategory(externalProduct.getCategory());
        product.setCategory(category);

        // Обработка рейтинга
        Rating rating = dtoMapper.ratingDtoToRating(externalProduct.getRating());
        ratingRepository.save(rating);
        product.assignRating(rating);

        return product;
    }

    private void updateProductFromExternalData(Product existingProduct, ImportProductDto externalProduct) {
        // Обновление основных полей
        existingProduct.setTitle(externalProduct.getTitle());
        existingProduct.setPrice(externalProduct.getPrice());
        existingProduct.setDescription(externalProduct.getDescription());

        // Обновление категории
        Category newCategory = getOrCreateCategory(externalProduct.getCategory());
        existingProduct.setCategory(newCategory);

        // Обновление рейтинга
        Rating existingRating = existingProduct.getRating();
        if (existingRating != null) {
            existingRating.setRate(externalProduct.getRating().getRate());
            existingRating.setCount(externalProduct.getRating().getCount());
            ratingRepository.save(existingRating);
        } else {
            Rating newRating = dtoMapper.ratingDtoToRating(externalProduct.getRating());
            ratingRepository.save(newRating);
            existingProduct.assignRating(newRating);
        }
    }

    private Category getOrCreateCategory(String categoryTitle) {
        return categoryRepository.findByTitle(categoryTitle)
                .orElseGet(() -> {
                    Category newCategory = new Category(categoryTitle);
                    return categoryRepository.save(newCategory);
                });
    }
}
