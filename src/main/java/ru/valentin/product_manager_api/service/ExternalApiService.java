package ru.valentin.product_manager_api.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.valentin.product_manager_api.dto.ImportProductDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final Validator validator;

    @Value("${external.api.url}")
    private String externalApiUrl;

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    public ExternalApiService(RestTemplate restTemplate, Validator validator) {
        this.restTemplate = restTemplate;
        this.validator = validator;
    }

    public List<ImportProductDto> fetchProductsFromExternalApi() {
        try {
            ResponseEntity<ImportProductDto[]> response = restTemplate.getForEntity(
                    externalApiUrl,
                    ImportProductDto[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ImportProductDto> validatedProducts =
                        validateProducts(Arrays.asList(response.getBody()));
                log.info("Получено {} товаров из внешнего API", response.getBody().length);
                return validatedProducts;
            } else {
                log.error("Ошибка при получении данных из внешнего API: {}", response.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Исключение при запросе к внешнему API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<ImportProductDto> validateProducts(List<ImportProductDto> products) {
        List<ImportProductDto> validProducts = new ArrayList<>();

        for (ImportProductDto product : products) {
            Set<ConstraintViolation<ImportProductDto>> violations = validator.validate(product);

            if (violations.isEmpty()) {
                validProducts.add(product);
            } else {
                log.warn("Товар с ID {} не прошел валидацию: {}",
                        product.getId(),
                        violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", ")));
            }
        }

        return validProducts;
    }
}
