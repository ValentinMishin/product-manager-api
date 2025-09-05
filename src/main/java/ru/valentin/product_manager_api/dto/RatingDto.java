package ru.valentin.product_manager_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO для рейтинга товара")
public class RatingDto {
    @Schema(description = "Средняя оценка товара", example = "7.8")
    @NotNull(message = "Значение рейтинга обязательно")
    @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
    @DecimalMax(value = "10.0", message = "Рейтинг не может быть больше 10")
    @JsonProperty("rate")
    private Double rate;

    @Schema(description = "Количество оценок", example = "120")
    @NotNull(message = "Количество оценок обязательно")
    @Min(value = 0, message = "Количество оценок не может быть отрицательным")
    @JsonProperty("count")
    private Integer count;

    public RatingDto() {
    }

    public RatingDto(Double rate, Integer count) {
        this.rate = rate;
        this.count = count;
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
        return "RatingDto{" +
                "rate=" + rate +
                ", count=" + count +
                '}';
    }
}
