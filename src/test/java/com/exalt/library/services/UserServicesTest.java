//package com.exalt.library.services;
//
//import com.exalt.library.dto.LoginDTO;
//import com.exalt.library.dto.RegisterDTO;
//import com.exalt.library.models.users.Borrower;
//import com.exalt.library.models.users.Role;
//import com.exalt.library.models.users.User;
//import com.exalt.library.repositories.BorrowerRepository;
//import com.exalt.library.repositories.UserRepository;
//import com.exalt.library.security.UserPrincipal;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link UserServices}.
// * @author Mohammad Rimawi
// */
//@ExtendWith(MockitoExtension.class)
//public class UserServicesTest {
//    @Mock private UserRepository userRepository;
//    @Mock private BorrowerRepository borrowerRepository;
//    @Mock private PasswordEncoder passwordEncoder;
//    @Mock private JwtService jwtService;
//    @Mock private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private UserServices userServices;
//
//    /**
//     * Configures the librarian registration key before each test.
//     */
//    @BeforeEach
//    void setSecretRegistrationKey() {
//        ReflectionTestUtils.setField(userServices, "librarianRegistrationKey", "the-real-secret");
//    }
//
//    /**
//     * Users without a registration key should be registered as borrowers.
//     */
//    @Test
//    void register_createsBorrowerRoleUser_whenNoKeyProvided() {
//        RegisterDTO dto = new RegisterDTO("Test User", "test@test.com", "password123", "+97012412", null);
//        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
//        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
//        when(borrowerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//
//        User result = userServices.register(dto);
//
//        assertEquals(Role.BORROWER, result.getRole());
//        assertNotNull(result.getBorrower());
//        verify(borrowerRepository).save(any(Borrower.class));
//    }
//
//    /**
//     * The correct registration key should create a librarian user.
//     */
//    @Test
//    void register_createsLibrarianUser_whenCorrectKeyProvided() {
//        RegisterDTO dto = new RegisterDTO(null, "librarian@test.com", "password123", "+5125161","the-real-secret");
//        when(userRepository.existsByEmail("librarian@test.com")).thenReturn(false);
//        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
//        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//
//        User result = userServices.register(dto);
//
//        assertEquals(Role.LIBRARIAN, result.getRole());
//        assertNull(result.getBorrower());
//        verify(borrowerRepository, never()).save(any());
//    }
//
//    /**
//     * An invalid registration key should fall back to the borrower role.
//     */
//    @Test
//    void register_createsBorrowerRole_whenKeyIsWrong() {
//        RegisterDTO dto = new RegisterDTO("Test User", "test@test.com", "password123", "+12351515","wrong-key");
//        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
//        when(passwordEncoder.encode(any())).thenReturn("hashed-password");
//        when(borrowerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//
//        User result = userServices.register(dto);
//
//        // a wrong key silently falls back to BORROWER, never errors - by design (Section 4 of the AuthN PDF)
//        assertEquals(Role.BORROWER, result.getRole());
//    }
//
//    /**
//     * Duplicate email addresses should be rejected.
//     */
//    @Test
//    void register_throws_whenEmailAlreadyExists() {
//        RegisterDTO dto = new RegisterDTO("Test User", "existing@test.com", "password123", "+1241512",null);
//        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);
//
//        assertThrows(IllegalArgumentException.class, () -> userServices.register(dto));
//
//        verify(userRepository, never()).save(any());
//    }
//
//    /**
//     * Valid credentials should return a JWT.
//     */
//    @Test
//    void login_returnsToken_whenCredentialsValid() {
//        LoginDTO dto = new LoginDTO("test@test.com", "password123");
//
//        User user = new User();
//        user.setEmail("test@test.com");
//        user.setRole(Role.BORROWER);
//        UserPrincipal principal = new UserPrincipal(user);
//
//        Authentication authResult = mock(Authentication.class);
//        when(authResult.getPrincipal()).thenReturn(principal);
//        when(authenticationManager.authenticate(any())).thenReturn(authResult);
//        when(jwtService.generateToken("test@test.com", "BORROWER")).thenReturn("fake-jwt-token");
//
//        String token = userServices.login(dto);
//
//        assertEquals("fake-jwt-token", token);
//    }
//
//    /**
//     * Invalid credentials should throw an exception.
//     */
//    @Test
//    void login_throws_whenCredentialsInvalid() {
//        LoginDTO dto = new LoginDTO("test@test.com", "wrong-password");
//        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
//
//        assertThrows(IllegalArgumentException.class, () -> userServices.login(dto));
//    }
//}
