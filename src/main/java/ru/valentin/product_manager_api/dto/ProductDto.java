package ru.valentin.product_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "DTO для товара")
public class ProductDto {
    @Schema(description = "Название товара", example = "Хлеб пшеничноржаной")
    @NotBlank(message = "Название товара обязательно")
    @Size(max = 50, message = "Название товара не должно превышать 50 символов")
    @JsonProperty("title")
    protected String title;

    @Schema(description = "Цена товара", example = "50.50")
    @NotNull(message = "Цена товара обязательна")
    @PositiveOrZero(message = "Цена товара не может быть отрицательной")
    @JsonProperty("price")
    protected BigDecimal price;

    @Schema(description = "Описание товара", example = "Хлеб 400 грамм, из ржаной муки")
    @Size(max = 255, message = "Описание товара не должно превышать 255 символов")
    @NotBlank(message = "Описание товара обязательно")
    @JsonProperty("description")
    protected String description;

    @Schema(description = "Название категории товара", example = "Хлебобулочные изделия")
    @NotBlank(message = "Категория товара обязательна")
    @Size(max = 50, message = "Название категории не должно превышать 50 символов")
    @JsonProperty("category")
    protected String category;

    @Schema(description = "Рейтинг товара")
    @NotNull(message = "Рейтинг товара обязателен")
    @Valid
    @JsonProperty("rating")
    protected RatingDto rating;

    public ProductDto() {
    }

    public ProductDto(String title, BigDecimal price, String description, String category, RatingDto rating) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RatingDto getRating() {
        return rating;
    }

    public void setRating(RatingDto rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
