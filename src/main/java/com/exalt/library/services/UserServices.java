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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Value("${library.librarian-registration-key}")
    private String librarianRegistrationKey;

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
     * resolves the role first (based on the registration key),
     * then validates, then creates the User and, if BORROWER, a linked Borrower too
     */
    @Override
    public User register(RegisterDTO registerDTO) {
        Role role = resolveRole(registerDTO.registrationKey());
        RegisterValidator.validate(registerDTO, role);

        if (userExists(registerDTO.email())) {
            throw new ConflictException("A user with this email already exists");
        }

        User user = new User();
        user.setEmail(registerDTO.email());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setRole(role);

        if (role == Role.BORROWER) {
            Borrower borrower = new Borrower();
            borrower.setName(registerDTO.name());
            borrower.setPhoneNumber(registerDTO.phoneNumber());
            Borrower savedBorrower = borrowerRepository.save(borrower);
            user.setBorrower(savedBorrower);
        }

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
     * decides the role for a new registration -
     * LIBRARIAN only if the correct registration key was supplied, BORROWER otherwise (including any wrong/missing key)
     * @param registrationKey
     * @return
     */
    private Role resolveRole(String registrationKey) {
        if (registrationKey != null && !registrationKey.isBlank() && registrationKey.equals(librarianRegistrationKey)) {
            return Role.LIBRARIAN;
        }
        return Role.BORROWER;
    }
}
