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
	
	@NotBlank(message = "El código postal es obligatorio")
    @Size(max = 7, message = "El código postal no puede exceder 7 caracteres")
    private String zipCode;
	
	@NotBlank(message = "La provincia es obligatoria")
	@Size(max = 50, message = "La provincia no puede exceder 50 caracteres")
    private String province;
	
	@NotBlank(message = "La localidad es obligatoria")
	@Size(max = 50, message = "La localidad no puede exceder 50 caracteres")
    private String locality;
}
