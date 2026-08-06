package com.exalt.library.models.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * a class representing a user account used for authentication
 * links to a Borrower when the role is BORROWER; null for LIBRARIAN accounts
 * @author Mohammad Rimawi
 */
@Document(collection = "users")
public class User {
    @Id
    private String id; // Defines the identity number for a user account

    @Indexed(unique = true)
    private String email; // Defines the login identifier for the user

    private String name; // Defines the name of the user
    private String password; // Defines the hashed password for the user

    private Role role; // Defines the user's role (LIBRARIAN or BORROWER)

    @DocumentReference
    private Borrower borrower; // Defines the linked borrower record, null for librarian-only accounts

    /**
     * A default constructor
     */
    public User() { }

//    ==== GETTERS ====
    /**
     * a method for getting the ID
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * a method for getting the email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * a method for getting the name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * a method for getting the password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * a method for getting the role
     * @return
     */
    public Role getRole() {
        return role;
    }

    /**
     * a method for getting the borrower
     * @return
     */
    public Borrower getBorrower() {
        return borrower;
    }
//    ==== GETTERS ====

//    ==== SETTERS ====

    /**
     * a method for setting the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * a method for setting the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * a method for setting the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * a method for setting the role
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * a method for setting the borrower
     * @param borrower
     */
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }
//    ==== SETTERS ====

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}