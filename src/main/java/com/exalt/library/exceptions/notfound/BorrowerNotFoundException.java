package com.exalt.library.exceptions.notfound;
//Fixed

/**
 * a class that throws an unchecked exception if there was no borrower found
 * @author Mohammad Rimawi
 */
public class BorrowerNotFoundException extends RuntimeException {
    /**
     * a parameterized constructor to define the message that comes from the parent class
     * @param message
     */
    public BorrowerNotFoundException(String message) {
        super(message);
    }
}
