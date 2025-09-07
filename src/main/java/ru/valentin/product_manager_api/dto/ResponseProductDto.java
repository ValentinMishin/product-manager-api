package ru.valentin.product_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO для товара в ответ на создание или обновление")
public class ResponseProductDto extends ProductDto{
    @Schema(description = "Идентификатор товара в локальной системе", example = "1")
    @NotNull(message = "ID товара обязательно")
    @Positive(message = "ID товара должен быть положительным числом")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Идентификатор товара во внешней системе", example = "1")
    @Positive(message = "ID товара должен быть положительным числом")
    @JsonProperty("externalId")
    private Long externalId;

    public ResponseProductDto() {
    }

    public ResponseProductDto(String title, BigDecimal price, String description, String category, RatingDto rating, Long id, Long externalId) {
        super(title, price, description, category, rating);
        this.id = id;
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        return "ResponseProductDto{" +
                "id=" + id +
                ", externalId=" + externalId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                '}';
    }
}
