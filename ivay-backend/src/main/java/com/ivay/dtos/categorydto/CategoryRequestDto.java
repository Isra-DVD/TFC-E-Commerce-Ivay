package com.ivay.dtos.categorydto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a category.
 *
 * Contains the name of the category with validation constraints
 * to ensure it is provided and does not exceed the length limit.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CategoryRequestDto {

    /**
     * Name of the category.
     * Must not be null and must not exceed 50 characters.
     */
    @NotNull(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre de la categoría no puede exceder los 50 caracteres")
    private String name;
}
