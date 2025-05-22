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

/**
 * Represents an API error response with status, message, details, and timestamp.
 *
 * When an exception occurs, instances of this class are returned
 * to the client, providing standardized error information.
 *
 * Fields include:
 * - status: HTTP status code of the error
 * - message: brief description of the error
 * - errors: list of specific error details
 * - timestamp: date and time when the error was generated
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    /**
     * HTTP status code of the error.
     */
    @Schema(example = "404", description = "Status error code")
    @NonNull
    private HttpStatus status;

    /**
     * Brief description of the error.
     */
    @NonNull
    private String message;

    /**
     * Detailed list of error messages or validation failures.
     */
    private List<String> errors;

    /**
     * Timestamp when the error was generated, formatted as dd/MM/yy hh:mm:ss.
     */
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yy hh:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Constructs an ApiError with a list of error details.
     *
     * @param status  HTTP status code of the error
     * @param message brief description of the error
     * @param errors  list of specific error messages
     */
    public ApiError(@NonNull HttpStatus status,
                    @NonNull String message,
                    List<String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Constructs an ApiError with a single error detail.
     *
     * @param status  HTTP status code of the error
     * @param message brief description of the error
     * @param error   a single error message
     */
    public ApiError(@NonNull HttpStatus status,
                    @NonNull String message,
                    String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
    }
}
