package com.ivay.dtos.api;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic wrapper for API responses.
 *
 * Provides a consistent structure for responses, including:
 * - timestamp: when the response was created
 * - message: human-readable status or informational message
 * - code: numeric status code
 * - data: the payload of the response
 *
 * @param <T> the type of the response payload
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDto<T> {

    /**
     * The timestamp when the response was generated.
     */
    private LocalDateTime timestamp;

    /**
     * A message describing the result of the operation.
     */
    private String message;

    /**
     * Numeric status code representing the outcome.
     */
    private int code;

    /**
     * The data payload returned by the API.
     */
    private T data;

    /**
     * Constructs a new ApiResponseDto with the given message, code, and data.
     * The timestamp is set to the current date and time.
     *
     * @param message human-readable description of the response
     * @param code    numeric status code of the response
     * @param data    payload to include in the response
     */
    public ApiResponseDto(String message, int code, T data) {
        this.timestamp = LocalDateTime.now();
        this.message   = message;
        this.code      = code;
        this.data      = data;
    }
}
