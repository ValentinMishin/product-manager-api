package ru.valentin.product_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "DTO для товара из внешнего API")
public class ImportProductDto extends ProductDto{
    @Schema(description = "Идентификатор товара во внешней системе", example = "1")
    @NotNull(message = "ID товара обязательно")
    @Positive(message = "ID товара должен быть положительным числом")
    @JsonProperty("id")
    private Long id;

    public ImportProductDto() {}

    public ImportProductDto(String title, BigDecimal price, String description, String category, RatingDto rating, Long id) {
        super(title, price, description, category, rating);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImportProductDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                '}';
    }
}
