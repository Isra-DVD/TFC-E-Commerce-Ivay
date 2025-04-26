package com.ivay.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ivay.dtos.api.ApiError;
import com.ivay.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

	// 404 - Endpoint no encontrado
	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, 
    															   final HttpHeaders headers, 
    															   final HttpStatusCode status,
    															   final WebRequest request) {
        log.info(ex.getClass().getName());
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	// 400 - Validación con @Valid	
	 @Override
	 protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
	                                                                  final HttpHeaders headers,
	                                                                  final HttpStatusCode status,
	                                                                  final WebRequest request) {
	        log.info(ex.getClass().getName());
	        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
	            .map(err -> err.getDefaultMessage())
	            .collect(Collectors.toList());
	        String message = errors.isEmpty() ? "Validation failed" : errors.get(0);
	        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, errors);
	        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	    }
	
	// 404 - Error personalizado, también se podría sobreescribir el general
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex,     											
    																WebRequest request) {
		
		log.info(ex.getClass().getName());
		
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "Resource Not Found");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(final ConstraintViolationException ex,
                                                                       final WebRequest request) {
        log.info(ex.getClass().getName());
        List<String> errors = ex.getConstraintViolations().stream()
            .map(err -> err.getMessage())
            .collect(Collectors.toList());
        String message = errors.isEmpty() ? "Validation error" : errors.get(0);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
}
