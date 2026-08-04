package com.exalt.library.exceptions;

/**
 * a class that throws an unchecked exception when a request conflicts with the current
 * state of a resource - e.g. a duplicate entity, or an item that's currently unavailable
 * @author Mohammad Rimawi
 */
public class ConflictException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public ConflictException(String message) {
        super(message);
    }
}