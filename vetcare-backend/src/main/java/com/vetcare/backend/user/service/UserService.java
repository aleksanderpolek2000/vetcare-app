package com.vetcare.backend.user.service;

import com.vetcare.backend.user.dto.CreateUserRequest;
import com.vetcare.backend.user.dto.UserResponse;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
