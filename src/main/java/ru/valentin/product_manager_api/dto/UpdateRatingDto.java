package ru.valentin.product_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

@Schema(description = "DTO для обновления рейтинга товара")
public class UpdateRatingDto {
    @Schema(description = "Средняя оценка товара", example = "4.2", nullable = true)
    @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
    @DecimalMax(value = "10.0", message = "Рейтинг не может быть больше 10")
    private Double rate;

    @Schema(description = "Количество оценок", example = "150", nullable = true)
    @Min(value = 0, message = "Количество оценок не может быть отрицательным")
    private Integer count;

    // Проверка, что хотя бы одно поле заполнено
    public boolean hasAtLeastOneField() {
        return rate != null || count != null;
    }

    public UpdateRatingDto(Double rate, Integer count) {
        this.rate = rate;
        this.count = count;
    }

    public UpdateRatingDto() {
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UpdateRatingDto{" +
                "rate=" + rate +
                ", count=" + count +
                '}';
    }
}
