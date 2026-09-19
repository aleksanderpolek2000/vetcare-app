package com.vetcare.backend.user.controller;

import com.vetcare.backend.user.dto.AuthResponse;
import com.vetcare.backend.user.dto.CreateUserRequest;
import com.vetcare.backend.user.dto.LoginRequest;
import com.vetcare.backend.user.dto.UserResponse;
import com.vetcare.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

        UserResponse createUser = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = userService.login(request);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMe(Authentication authentication) {
        return ResponseEntity.ok("Zalogowany użytkownik: " + authentication.getName());
    }

}
