package com.vetcare.backend.user.controller;

import com.vetcare.backend.user.dto.CreateUserRequest;
import com.vetcare.backend.user.dto.UserResponse;
import com.vetcare.backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

        UserResponse createUser = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }
}
