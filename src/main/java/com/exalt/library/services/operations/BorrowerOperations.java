package com.exalt.library.services.operations;

import com.exalt.library.exceptions.notfound.ItemNotFoundException;
import com.exalt.library.models.users.Borrower;

import java.util.List;

/**
 * an interface representing the operations for a borrower in a library
 * @author Mohammad Rimawi
 */
public interface BorrowerOperations {

    /**
     * a method used to get all the borrowers
     * implemented inside BorrowerServices
     * @return the list of all borrowers
     */
    List<Borrower> getAllBorrowers();

    /**
     * a method used to find a specific borrower based on his/her id
     * implemented inside BorrowerServices
     * @param id
     * @return return a borrower if he/she exists
     * @throws ItemNotFoundException if the borrower doesn't exist
     */
    Borrower findBorrowerById(String id);

    /**
     * a method for checking if the borrower exists or not
     * implemented inside BorrowerServices
     * @param id - represents the borrower id
     * @return - returns true or false based if it exists in the DB or not
     */
    boolean borrowerExists(String id);

    /**
     * a method for checking how many borrowers exist
     * implemented inside BorrowerServices
     * @return - the count
     */
    int getBorrowerCount();
}