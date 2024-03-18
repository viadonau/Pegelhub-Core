package com.stm.pegelhub.logic.exception;

/**
 * Exception for objects that were requested but do not exist.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
