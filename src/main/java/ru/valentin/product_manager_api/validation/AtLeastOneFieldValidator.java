package ru.valentin.product_manager_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.valentin.product_manager_api.dto.UpdateProductDto;

public class AtLeastOneFieldValidator implements
        ConstraintValidator<AtLeastOneField, UpdateProductDto> {

    @Override
    public boolean isValid(UpdateProductDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return false;
        }

        boolean hasProductField = dto.getTitle() != null ||
                dto.getPrice() != null ||
                dto.getDescription() != null ||
                dto.getCategoryId() != null;

        boolean hasRatingField = dto.getRating() != null
                && dto.getRating().hasAtLeastOneField();

        return hasProductField || hasRatingField;
    }
}
