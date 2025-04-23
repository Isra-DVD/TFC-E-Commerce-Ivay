package com.ivay.dtos.api;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDto<T> {

	private LocalDateTime timestamp;
	private String message;
	private int code;
	private T data;
	
	public ApiResponseDto(String message, int code, T data ) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.code = code;
		this.data = data;
	}

}