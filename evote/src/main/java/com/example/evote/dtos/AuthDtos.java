package com.example.evote.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public static class LoginRequest {
        @Email @NotBlank public String email;
        @NotBlank public String password;
    }

    public static class JwtResponse {
        public String token;
        public String role;
        public Long adminId;
        public Long divisionId; // may be null
        public JwtResponse(String token, String role, Long adminId, Long divisionId) {
            this.token = token; this.role=role; this.adminId=adminId; this.divisionId = divisionId;
        }
    }
}
