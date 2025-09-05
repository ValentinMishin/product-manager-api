package ru.valentin.product_manager_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.valentin.product_manager_api.dto.ImportProductDto;
import ru.valentin.product_manager_api.dto.mapper.ProductMapper;
import ru.valentin.product_manager_api.model.Category;
import ru.valentin.product_manager_api.model.Product;
import ru.valentin.product_manager_api.model.Rating;
import ru.valentin.product_manager_api.repository.CategoryRepository;
import ru.valentin.product_manager_api.repository.ProductRepository;
import ru.valentin.product_manager_api.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ExternalApiService externalApiService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;
    private final ProductMapper productMapper;

    public ProductService(ExternalApiService externalApiService, ProductRepository productRepository, CategoryRepository categoryRepository, RatingRepository ratingRepository, ProductMapper productMapper) {
        this.externalApiService = externalApiService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.ratingRepository = ratingRepository;
        this.productMapper = productMapper;
    }

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
        Product product = productMapper.importProductDtoToProduct(externalProduct);

        // Обработка категории
        Category category = getOrCreateCategory(externalProduct.getCategory());
        product.setCategory(category);

        // Обработка рейтинга
        Rating rating = productMapper.ratingDtoToRating(externalProduct.getRating());
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
            Rating newRating = productMapper.ratingDtoToRating(externalProduct.getRating());
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
