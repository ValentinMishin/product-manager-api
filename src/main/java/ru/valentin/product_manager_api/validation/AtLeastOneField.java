package ru.valentin.product_manager_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneField {
    String message() default "Хотя бы одно поле должно быть заполнено в UpdateProductDto или UpdateRatingDto";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
