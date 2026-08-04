package com.exalt.library.services;

import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
import com.exalt.library.models.users.Borrower;
import com.exalt.library.repositories.BorrowerRepository;
import com.exalt.library.services.operations.BorrowerOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * a class representing the services for the borrower
 * implements the interface BorrowerOperations
 * @author Mohammad Rimawi
 */
@Service
public class BorrowerServices implements BorrowerOperations {
    private final BorrowerRepository borrowerRepository; // Defines the borrower repository

    /**
     * constructor injection
     * @param borrowerRepository
     */
    public BorrowerServices(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    /**
     * a method for getting all the borrowers
     * @return the list of all borrowers
     */
    @Override
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    /**
     * a method used to find a specific borrower based on his/her id
     * @param id
     * @return the borrower if found
     * @throws BorrowerNotFoundException if no borrower matches the id
     */
    @Override
    public Borrower findBorrowerById(String id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found"));
    }

    /**
     * a method for checking if the borrower exists or not
     * @param id
     * @return true or false based on whether it exists
     */
    @Override
    public boolean borrowerExists(String id) {
        return borrowerRepository.existsById(id);
    }

    /**
     * a method for checking how many borrowers exist
     * @return the count
     */
    @Override
    public int getBorrowerCount() {
        return (int) borrowerRepository.count();
    }
}