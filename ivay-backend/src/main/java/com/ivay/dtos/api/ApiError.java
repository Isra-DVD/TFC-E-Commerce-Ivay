package com.ivay.dtos.api;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

	@Schema(example = "404", description = "Status error code")
	@NonNull
	HttpStatus status;
	
	@NonNull
	private String message;
	
	private List<String> errors;
	
	@JsonFormat(shape=Shape.STRING, pattern="dd/MM/yy hh:mm:ss")
	private LocalDateTime timestamp; //= LocalDateTime.now();
	
	
	public ApiError(final HttpStatus status, final String message, final List<String> errors) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
		this.errors = errors;		
	}
	
	public ApiError(final HttpStatus status, final String message, final String error) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
		this.errors = Arrays.asList(error);		
	}
	
}

