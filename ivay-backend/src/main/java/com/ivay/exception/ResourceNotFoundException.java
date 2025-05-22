package com.ivay.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 *
 * This exception indicates that an entity or resource requested by the client
 * does not exist in the system.
 *
 * @since 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message explaining why the resource was not found
     * @param cause   the original exception that led to this error, if any
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
