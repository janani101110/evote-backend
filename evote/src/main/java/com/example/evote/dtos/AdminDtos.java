package com.example.evote.dtos;

import com.example.evote.Entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
 
public class AdminDtos {
    public static class CreateAdminRequest {
        @NotBlank public String fullName;
        @Email @NotBlank public String email;
        @NotBlank @Size(min=8) public String password;
        @NotNull public Role role;
        public Long divisionId; // required if role == DIVISIONAL_ADMIN
    }

    public static class AdminResponse {
        public Long id; public String fullName; public String email; public String role; public Long divisionId; public boolean active;
        public AdminResponse(Long id, String fullName, String email, String role, Long divisionId, boolean active) {
            this.id=id; this.fullName=fullName; this.email=email; this.role=role; this.divisionId=divisionId; this.active=active;
        }
    }
}
