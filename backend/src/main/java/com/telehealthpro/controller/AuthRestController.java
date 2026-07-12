package com.telehealthpro.controller;

import com.telehealthpro.dto.MessageResponse;
import com.telehealthpro.dto.UserRegistrationDto;
import com.telehealthpro.dto.UserResponseDto;
import com.telehealthpro.security.UserPrincipal;
import com.telehealthpro.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Registration and "who am I" endpoints.
 * Actual login/logout is handled directly by Spring Security's form login
 * filter chain at POST /api/auth/login and POST /api/auth/logout (see SecurityConfig).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Registration successful! You can now log in."));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> currentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.toResponseDto(userService.findById(principal.getId())));
    }
}
