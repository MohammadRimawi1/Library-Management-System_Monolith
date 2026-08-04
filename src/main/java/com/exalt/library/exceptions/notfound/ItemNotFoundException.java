package com.exalt.library.exceptions.notfound;

//Fixed

/**
 * a class that throws an unchecked exception if there was no item found
 * @author Mohammad Rimawi
 */
public class ItemNotFoundException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}
