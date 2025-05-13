package com.ivay.dtos.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProfileRequestDto {
	@NotBlank(message = "El nombre de la cuenta no puede estar vacio")
	@Size(max = 100, message = "El nombre de la cuenta no puede exceder 100 caracteres")
	private String name;
	
	@NotBlank(message = "El nombre completo del usuario no puede estar vacio")
	@Size(max = 50, message = "El nombre completo del usuario no puede exceder 50 caracteres")
	private String fullName;

	@NotBlank(message = "El email del usuario no puede estar vacio")
	@Email
	@Size(max = 50, message = "El email del usuario no puede exceder 50 caracteres")
	private String email;

	@NotBlank(message = "El teléfono del usuario no puede estar vacio")
	@Size(max = 20, message = "El teléfono del usuario no puede exceder 50 caracteres")
	private String phone;
	
	@NotBlank(message = "La dirección del usuario no puede estar vacia")
	@Size(max = 255, message = "La dirección del usuario no puede exceder 255 caracteres")
	private String address;
}
