package com.exalt.library.services;

import com.exalt.library.dto.LoginDTO;
import com.exalt.library.dto.RegisterDTO;
import com.exalt.library.exceptions.AuthenticationFailedException;
import com.exalt.library.exceptions.ConflictException;
import com.exalt.library.exceptions.notfound.UserNotFoundException;
import com.exalt.library.models.users.Borrower;
import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;
import com.exalt.library.repositories.BorrowerRepository;
import com.exalt.library.repositories.UserRepository;
import com.exalt.library.security.UserPrincipal;
import com.exalt.library.services.operations.UserOperations;
import com.exalt.library.validation.LoginValidator;
import com.exalt.library.validation.RegisterValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * a class representing the services for the users
 * implements the interface UserOperations
 * @author Mohammad Rimawi
 */
@Service
public class UserServices implements UserOperations {
    private final UserRepository userRepository; // Defines the user repository
    private final BorrowerRepository borrowerRepository; // Defines the borrower repository
    private final PasswordEncoder passwordEncoder; // defines the password encoder
    private final JwtService jwtService; // Defines the jwt service
    private final AuthenticationManager authenticationManager; // defines the authenticationManager

    /**
     * constructor injection
     * @param userRepository
     * @param borrowerRepository
     * @param passwordEncoder
     */
    public UserServices(
            UserRepository userRepository,
            BorrowerRepository borrowerRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.borrowerRepository = borrowerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * registers a new user
     * always creates a BORROWER - librarians and the admin are never created through registration
     * validates, then creates the User and a linked Borrower
     */
    @Override
    public User register(RegisterDTO registerDTO) {
        RegisterValidator.validate(registerDTO);

        if (userExists(registerDTO.email())) {
            throw new ConflictException("A user with this email already exists");
        }

        User user = new User();
        user.setName(registerDTO.name());
        user.setEmail(registerDTO.email());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setRole(Role.BORROWER);

        Borrower borrower = new Borrower();
        borrower.setName(registerDTO.name());
        borrower.setPhoneNumber(registerDTO.phoneNumber());
        Borrower savedBorrower = borrowerRepository.save(borrower);
        user.setBorrower(savedBorrower);

        return userRepository.save(user);
    }

    /**
     * finds a user by their email
     * @param email
     * @return the user if found
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * checks whether a user with the given email already exists
     * @param email
     * @return true or false
     */
    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * verifies credentials and issues a JWT if they're correct
     * @param loginDTO
     * @return
     */
    public String login(LoginDTO loginDTO) {
        LoginValidator.validate(loginDTO);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
            );
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid email or password");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return jwtService.generateToken(principal.getUsername(), principal.getUser().getRole().name());
    }

    /**
     * promotes an existing borrower to librarian
     * librarians have no borrower link, so the linked Borrower document is deleted
     * @param userId
     * @return the updated user
     */
    @Override
    public User promoteToLibrarian(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != Role.BORROWER) {
            throw new ConflictException("Only borrowers can be promoted to librarian");
        }

        Borrower borrower = user.getBorrower();
        if (borrower != null) {
            user.setName(borrower.getName());
        }

        user.setRole(Role.LIBRARIAN);
        user.setBorrower(null);
        User savedUser = userRepository.save(user);

        if (borrower != null) {
            borrowerRepository.deleteById(borrower.getId());
        }

        return savedUser;
    }

    /**
     * fetches users, optionally filtered by role
     * @param role if non-null, restricts results to that role; if null, returns all users
     * @return
     */
    @Override
    public List<User> getUsers(Role role) {
        return role != null ? userRepository.findByRole(role) : userRepository.findAll();
    }

    /**
     * demotes an existing librarian back to borrower
     * a new Borrower document is created since promotion deletes the old one -
     * phoneNumber comes back null, there's currently no way to recover it
     * @param userId
     * @return the updated user
     */
    @Override
    public User demoteToBorrower(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() != Role.LIBRARIAN) {
            throw new ConflictException("Only librarians can be demoted to borrower");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new ConflictException("Cannot demote: user has no name on record");
        }

        Borrower borrower = new Borrower();
        borrower.setName(user.getName());
        Borrower savedBorrower = borrowerRepository.save(borrower);

        user.setRole(Role.BORROWER);
        user.setBorrower(savedBorrower);
        return userRepository.save(user);
    }
}