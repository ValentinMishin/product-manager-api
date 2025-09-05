package ru.valentin.product_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для категории товара")
public class ResponseCategoryDto {
    @Schema(description = "Идентификатор категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Хлебобулочные изделия")
    private String title;

    public ResponseCategoryDto() {
    }

    public ResponseCategoryDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
