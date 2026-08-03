//package com.exalt.library.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
///**
// * Unit tests for {@link JwtService}
// * @author Mohammad Rimawi
// */
//public class JwtServicesTest {
//    private JwtService jwtService;
//
//    /**
//     * Initializes the JWT service with test configuration before each test.
//     */
//    @BeforeEach
//    void setUp() {
//        jwtService = new JwtService();
//        ReflectionTestUtils.setField(jwtService, "secret",
//                "a-long-enough-test-secret-key-that-is-definitely-at-least-64-bytes-for-hs512");
//        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
//    }
//
//    /**
//     * Generated tokens should contain the original email.
//     */
//    @Test
//    void generateToken_thenExtractEmail_returnsOriginalEmail() {
//        String token = jwtService.generateToken("test@test.com", "BORROWER");
//
//        assertEquals("test@test.com", jwtService.extractEmail(token));
//    }
//
//    /**
//     * Generated tokens should contain the original user role.
//     */
//    @Test
//    void generateToken_thenExtractRole_returnsOriginalRole() {
//        String token = jwtService.generateToken("test@test.com", "LIBRARIAN");
//
//        assertEquals("LIBRARIAN", jwtService.extractRole(token));
//    }
//
//    /**
//     * Freshly generated tokens should be valid.
//     */
//    @Test
//    void isValid_returnsTrue_forFreshlyGeneratedToken() {
//        String token = jwtService.generateToken("test@test.com", "BORROWER");
//
//        assertTrue(jwtService.isValid(token));
//    }
//
//    /**
//     * Invalid token strings should be rejected.
//     */
//    @Test
//    void isValid_returnsFalse_forGarbageString() {
//        assertFalse(jwtService.isValid("not.a.real.token"));
//    }
//
//    /**
//     * Tokens with a modified signature should be rejected.
//     */
//    @Test
//    void isValid_returnsFalse_forTamperedToken() {
//        String token = jwtService.generateToken("test@test.com", "BORROWER");
//        String tampered = token.substring(0, token.length() - 5) + "AAAAA"; // corrupt the signature
//
//        assertFalse(jwtService.isValid(tampered));
//    }
//}
