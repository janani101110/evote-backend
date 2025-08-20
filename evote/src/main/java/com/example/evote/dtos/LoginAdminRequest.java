package com.example.evote.dtos;

import jakarta.validation.constraints.NotBlank;

public class LoginAdminRequest {
    @NotBlank(message = "Email is required")
    public String email;

    @NotBlank(message = "Password is required")
    public String passwordHash; 

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
