package com.ivay.dtos.addressdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressRequestDto {
	
	@NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;
	
	@NotBlank(message = "La dirección es obligatoria")
	@Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;
}
