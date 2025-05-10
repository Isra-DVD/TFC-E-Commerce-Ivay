package com.ivay.dtos.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDto {
	
	private String accessToken;
	private String tokenType = "Bearer ";
	
	public AuthResponseDto(String accessToken) {
		this.accessToken = accessToken;
	}
}
