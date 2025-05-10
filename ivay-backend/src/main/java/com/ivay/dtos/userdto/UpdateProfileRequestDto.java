package com.ivay.dtos.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProfileRequestDto {
	@NotBlank @Size(max = 100)
	private String name;

	@NotBlank @Email @Size(max = 100)
	private String email;

	@NotBlank @Size(max = 20)
	private String phone;
}
