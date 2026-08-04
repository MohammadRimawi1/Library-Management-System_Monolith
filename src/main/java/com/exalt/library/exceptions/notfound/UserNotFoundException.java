package com.exalt.library.exceptions.notfound;

/**
 * a class that throws an unchecked exception if the User was unavailable
 * @author Mohammad Rimawi
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
