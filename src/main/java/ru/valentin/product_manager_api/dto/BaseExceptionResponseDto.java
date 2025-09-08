package ru.valentin.product_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BaseExceptionResponseDto {
    @Schema(description = "Статус ответа сервиса", example = "400")
    @JsonProperty("status")
    private String status;

    @Schema(description = "Пояснение к исключению", example = "Категория с ID 1 не найдена")
    @JsonProperty("message")
    private String message;

    public BaseExceptionResponseDto() {
    }

    public BaseExceptionResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseExceptionResponseDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
