package ru.valentin.product_manager_api.sheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.valentin.product_manager_api.service.ProductService;

@Component
public class ProductImportScheduler {
    private static final Logger log = LoggerFactory.getLogger(ProductImportScheduler.class);

    private final ProductService productService;

    public ProductImportScheduler(ProductService productService) {
        this.productService = productService;
    }

    @Scheduled(fixedRateString = "${app.import.interval}")
    public void scheduledImport() {
        log.info("Запуск импорта товаров");
        try {
            productService.importProducts();
            log.info("Импорт завершен успешно");
        } catch (Exception e) {
            log.error("Ошибка при импорте: {}", e.getMessage(), e);
        }
    }
}
