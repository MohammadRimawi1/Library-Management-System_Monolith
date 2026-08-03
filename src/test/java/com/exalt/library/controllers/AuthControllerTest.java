//package com.exalt.library.controllers;
//
//import com.exalt.library.dto.LoginDTO;
//import com.exalt.library.dto.RegisterDTO;
//import com.exalt.library.exceptions.handler.GlobalExceptionHandler;
//import com.exalt.library.models.users.Role;
//import com.exalt.library.models.users.User;
//import com.exalt.library.services.JwtService;
//import com.exalt.library.services.UserServices;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Unit tests for {@link AuthController}.
// * @author Mohammad Rimawi
// */
//@WebMvcTest(controllers = AuthController.class)
//@Import(GlobalExceptionHandler.class)
//class AuthControllerTest {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserServices userServices;
//
//    @MockitoBean
//    private JwtService jwtService;
//
//    /**
//     * Successful registration should return HTTP 201 with the user's email and role.
//     */
//    @Test
//    void register_returns201_withEmailAndRole() throws Exception {
//        RegisterDTO request = new RegisterDTO("Test User", "test@test.com", "password123", "+3q515125", null);
//
//        User created = new User();
//        created.setEmail("test@test.com");
//        created.setRole(Role.BORROWER);
//        when(userServices.register(request)).thenReturn(created);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.email").value("test@test.com"))
//                .andExpect(jsonPath("$.data.role").value("BORROWER"));
//    }
//
//    /**
//     * Registering with an existing email should return HTTP 400.
//     */
//    @Test
//    void register_returns400_whenEmailAlreadyExists() throws Exception {
//        RegisterDTO request = new RegisterDTO("Test User", "existing@test.com", "password123", "+2141251", null);
//
//        when(userServices.register(request))
//                .thenThrow(new IllegalArgumentException("A user with this email already exists"));
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("A user with this email already exists"));
//    }
//
//    /**
//     * Valid credentials should return HTTP 200 with a JWT.
//     */
//    @Test
//    void login_returns200_withToken() throws Exception {
//        LoginDTO request = new LoginDTO("test@test.com", "password123");
//
//        when(userServices.login(request)).thenReturn("fake-jwt-token");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"));
//    }
//
//    /**
//     * Invalid credentials should return HTTP 400.
//     */
//    @Test
//    void login_returns400_whenCredentialsInvalid() throws Exception {
//        LoginDTO request = new LoginDTO("test@test.com", "wrong-password");
//
//        when(userServices.login(request))
//                .thenThrow(new IllegalArgumentException("Invalid email or password"));
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Invalid email or password"));
//    }
//}