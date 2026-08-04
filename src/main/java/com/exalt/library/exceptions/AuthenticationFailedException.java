package com.exalt.library.exceptions;

/**
 * a class that throws an unchecked exception when login credentials are invalid -
 * the server doesn't recognize who's making the request
 * @author Mohammad Rimawi
 */
public class AuthenticationFailedException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public AuthenticationFailedException(String message) {
        super(message);
    }
}