package com.exalt.library.exceptions.notfound;

/**
 * a class that throws an unchecked exception if there was no reservation found
 * @author Mohammad Rimawi
 */
public class ReservationNotFoundException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
