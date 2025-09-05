package ru.valentin.product_manager_api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.valentin.product_manager_api.validation.AtLeastOneField;

import java.math.BigDecimal;

@AtLeastOneField(message = "Для обновления необходимо указать хотя бы одно поле")
@Schema(description = "DTO для обновления товара")
public class UpdateProductDto {
    @Schema(description = "Название товара", example = "Хлеб пшеничный", nullable = true)
    @Size(max = 50, message = "Название товара не должно превышать 50 символов")
    private String title;

    @Schema(description = "Цена товара", example = "99.99", nullable = true)
    @PositiveOrZero(message = "Цена товара не может быть отрицательной")
    private BigDecimal price;

    @Schema(description = "Описание товара", example = "Хлеб 300 грамм, из пшеничной муки", nullable = true)
    @Size(max = 255, message = "Описание товара не должно превышать 255 символов")
    private String description;

    @Schema(description = "ID категории товара", example = "2", nullable = true)
    @Positive(message = "ID категории должен быть положительным числом")
    private Long categoryId;

    @Schema(description = "Рейтинг товара", nullable = true)
    @Valid
    private UpdateRatingDto rating;

    public UpdateProductDto() {
    }

    public UpdateProductDto(String title, BigDecimal price, String description, Long categoryId, UpdateRatingDto rating) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public UpdateRatingDto getRating() {
        return rating;
    }

    public void setRating(UpdateRatingDto rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UpdateProductDto{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", rating=" + rating +
                '}';
    }
}
