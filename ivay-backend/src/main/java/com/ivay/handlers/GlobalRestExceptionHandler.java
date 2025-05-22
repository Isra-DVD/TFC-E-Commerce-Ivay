package com.ivay.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

/**
 * Global exception handler for REST controllers.
 *
 * Catches and translates exceptions into standardized error responses.
 *
 * Handles:
 * - NoHandlerFoundException for unmatched endpoints (404)
 * - MethodArgumentNotValidException for validation failures (400)
 * - ResourceNotFoundException for missing resources (404)
 * - ConstraintViolationException for constraint violations (400)
 * - IllegalArgumentException for invalid arguments (400)
 * - AccessDeniedException for insufficient permissions (403)
 *
 * @since 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles cases where no controller matches the request.
	 *
	 * Uses HTTP status 404 and returns an ApiError with details.
	 *
	 * @param ex the NoHandlerFoundException thrown by Spring
	 * @param headers the current HTTP headers
	 * @param status the HTTP status code
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 404
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {
		log.info(ex.getClass().getName());
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * Handles validation failures on method arguments annotated with @Valid.
	 *
	 * Uses HTTP status 400 and returns an ApiError with the first validation message
	 * and the list of all field error messages.
	 *
	 * @param ex the MethodArgumentNotValidException thrown during validation
	 * @param headers the current HTTP headers
	 * @param status the HTTP status code
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 400
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {
		log.info(ex.getClass().getName());
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(err -> err.getDefaultMessage())
				.collect(Collectors.toList());
		String message = errors.isEmpty() ? "Validation failed" : errors.get(0);
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	/**
	 * Handles custom ResourceNotFoundException when an entity is not found.
	 *
	 * Uses HTTP status 404 and returns an ApiError with a "Resource Not Found" detail.
	 *
	 * @param ex the ResourceNotFoundException thrown by application code
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 404
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException(
			ResourceNotFoundException ex,
			WebRequest request) {
		log.info(ex.getClass().getName());
		ApiError apiError = new ApiError(
				HttpStatus.NOT_FOUND,
				ex.getLocalizedMessage(),
				"Resource Not Found"
				);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * Handles ConstraintViolationException for validation on path and query parameters.
	 *
	 * Uses HTTP status 400 and returns an ApiError with the first violation message
	 * and the list of all violation messages.
	 *
	 * @param ex the ConstraintViolationException thrown during validation
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 400
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolationException(
			ConstraintViolationException ex,
			WebRequest request) {
		log.info(ex.getClass().getName());
		List<String> errors = ex.getConstraintViolations()
				.stream()
				.map(err -> err.getMessage())
				.collect(Collectors.toList());
		String message = errors.isEmpty() ? "Validation error" : errors.get(0);
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, errors);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * Handles IllegalArgumentException for invalid method arguments.
	 *
	 * Uses HTTP status 400 and returns an ApiError with the exception message.
	 *
	 * @param ex the IllegalArgumentException thrown by application code
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 400
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArg(
			IllegalArgumentException ex,
			WebRequest request) {
		log.info(ex.getClass().getName());
		ApiError apiError = new ApiError(
				HttpStatus.BAD_REQUEST,
				ex.getMessage(),
				List.of(ex.getMessage())
				);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * Handles AccessDeniedException when a user lacks necessary permissions.
	 *
	 * Uses HTTP status 403 and returns an ApiError indicating insufficient authorization.
	 *
	 * @param ex the AccessDeniedException thrown by Spring Security
	 * @param request the current web request
	 * @return a ResponseEntity containing the ApiError and status 403
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDeniedException(
			AccessDeniedException ex,
			WebRequest request) {
		log.info(ex.getClass().getName());
		ApiError apiError = new ApiError(
				HttpStatus.FORBIDDEN,
				"No tienes la autorización necesaria",
				List.of("No tienes la autorización necesaria")
				);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
