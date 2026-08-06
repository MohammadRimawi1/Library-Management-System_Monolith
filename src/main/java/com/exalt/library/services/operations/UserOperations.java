package com.exalt.library.services.operations;

import com.exalt.library.dto.LoginDTO;
import com.exalt.library.dto.RegisterDTO;
import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;

import java.util.List;

/**
 * an interface representing the operations for a user account
 * @author Mohammad Rimawi
 */
public interface UserOperations {

    /**
     * registers a new user
     * always creates a BORROWER - librarians and the admin are never created through registration
     * validates, then creates the User and a linked Borrower
     * @param registerDTO
     * @return
     */
    public User register(RegisterDTO registerDTO);

    /**
     * finds a user by their email
     * implemented inside UserServices
     * @param email
     * @return the user if found
     */
    User findByEmail(String email);

    /**
     * checks whether a user with the given email already exists
     * implemented inside UserServices
     * @param email
     * @return true or false
     */
    boolean userExists(String email);

    /**
     * verifies credentials and issues a JWT if they're correct
     * @param loginDTO
     * @return
     */
    public String login(LoginDTO loginDTO);

    /**
     * promotes an existing borrower to librarian
     * implemented inside UserServices
     * @param userId
     * @return the updated user
     */
    User promoteToLibrarian(String userId);

    /**
     * fetches users, optionally filtered by role
     * implemented inside UserServices
     * @param role if non-null, restricts results to that role; if null, returns all users
     * @return
     */
    List<User> getUsers(Role role);

    /**
     * demotes an existing librarian back to borrower
     * a new Borrower document is created since promotion deletes the old one -
     * phoneNumber comes back null, there's currently no way to recover it
     * implemented inside UserServices
     * @param userId
     * @return the updated user
     */
    User demoteToBorrower(String userId);
}