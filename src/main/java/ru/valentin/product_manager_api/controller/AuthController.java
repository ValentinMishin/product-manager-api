package ru.valentin.product_manager_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.valentin.product_manager_api.dto.UserRegistrationDto;
import ru.valentin.product_manager_api.model.User;
import ru.valentin.product_manager_api.model.UserRole;
import ru.valentin.product_manager_api.service.AuthService;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDto> registerUser(
            @Valid @RequestBody UserRegistrationDto request) {

        User user = authService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRoles()
        );

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                request.getUsername(), request.getPassword(),
                Set.of(UserRole.ROLE_USER)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationDto);
    }
}