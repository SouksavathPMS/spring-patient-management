package com.pm.authservice.controller;


import com.pm.authservice.dto.ApiResponse;
import com.pm.authservice.dto.request.LoginRequestDTO;
import com.pm.authservice.dto.response.LoginResponseDTO;
import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        Optional<String> optionalToken = authService.authenticate(loginRequestDTO);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Invalid credentials"
                    )
            );
        }

        String token = optionalToken.get();
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        LoginResponseDTO.builder()
                                .token(token)
                                .build(),
                        "Login successful"
                )
        );
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String authHeader) {
        // Authorization: Bearer <token>
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid token")
            );
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Token validated successfully")
                )
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.error(HttpStatus.UNAUTHORIZED.value(),
                                "Invalid token")
        );
    }
}
