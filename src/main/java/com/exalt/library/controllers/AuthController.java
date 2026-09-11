package com.exalt.library.controllers;

import com.exalt.library.dto.LoginDTO;
import com.exalt.library.dto.LoginResult;
import com.exalt.library.dto.RegisterDTO;
import com.exalt.library.models.users.User;
import com.exalt.library.services.JwtService;
import com.exalt.library.services.UserServices;
import com.exalt.library.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * a controller handling registration and login
 * @author Mohammad Rimawi
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserServices userServices; // defines user services
    private final JwtService jwtService; // defines jwt service

    /**
     * constructor injection
     * @param userServices
     */
    public AuthController(UserServices userServices, JwtService jwtService) {
        this.userServices = userServices;
        this.jwtService = jwtService;
    }

    /**
     * a method for registering for your account
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterDTO request) {
        LoginResult result = userServices.register(request);
        User user = result.user();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole());
        userMap.put("phoneNumber", user.getBorrower() != null ? user.getBorrower().getPhoneNumber() : null);

        return ResponseEntity.status(201).body(ApiResponse.success(201, Map.of(
                "token", result.token(),
                "user", userMap
        )));
    }

    /**
     * a method for logging in ur account
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO request) {
        LoginResult result = userServices.login(request);
        User user = result.user();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole());
        userMap.put("phoneNumber", user.getBorrower() != null ? user.getBorrower().getPhoneNumber() : null);

        return ResponseEntity.ok(ApiResponse.success(200, Map.of(
                "token", result.token(),
                "user", userMap
        )));
    }
}