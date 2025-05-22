package com.ivay.dtos.categorydto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDto {
	@NotNull(message = "El nombre de la categoría es obligatorio")
	@Size(max = 50, message = "El nombre de la categoría no puede exceder los 50 caracteres")
    private String name;
}
