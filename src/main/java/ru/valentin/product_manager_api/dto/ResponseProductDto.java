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
    @JsonProperty("external_id")
    private Long external_id;

    public ResponseProductDto() {
    }

    public ResponseProductDto(String title, BigDecimal price, String description, String category, RatingDto rating, Long id, Long external_id) {
        super(title, price, description, category, rating);
        this.id = id;
        this.external_id = external_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternal_id() {
        return external_id;
    }

    public void setExternal_id(Long external_id) {
        this.external_id = external_id;
    }

    @Override
    public String toString() {
        return "ResponseProductDto{" +
                "id=" + id +
                ", external_id=" + external_id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                '}';
    }
}
