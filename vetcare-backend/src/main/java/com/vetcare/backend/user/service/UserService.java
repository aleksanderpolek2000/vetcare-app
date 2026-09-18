package com.vetcare.backend.user.service;

import com.vetcare.backend.auth.service.JwtService;
import com.vetcare.backend.user.dto.AuthResponse;
import com.vetcare.backend.user.dto.CreateUserRequest;
import com.vetcare.backend.user.dto.LoginRequest;
import com.vetcare.backend.user.dto.UserResponse;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email " + request.email() + " jest już zajęty!");
        } else {

            UserEntity user = UserEntity.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .build();

            UserEntity savedUser = userRepository.save(user);

            return mapToResponse(savedUser);
        }
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Assert.hasText(request.email(), "Email nie może być pusty!");
        Assert.hasText(request.password(), "Hasło nie może być puste!");

        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy email lub hasło!"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Nieprawidłowy email lub hasło!");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail());
    }

    private static UserResponse mapToResponse(UserEntity userEntity) {
        Set<String> roles = Optional.ofNullable(userEntity.getRoles())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(roleEntity -> roleEntity.getName().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                roles,
                userEntity.getCreateAt());
    }

    ;
}
