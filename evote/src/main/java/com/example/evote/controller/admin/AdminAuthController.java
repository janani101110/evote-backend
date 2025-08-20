package com.example.evote.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.Admin;
import com.example.evote.Util.JwtAdminUtil;
import com.example.evote.dtos.AuthDtos.JwtResponse;
import com.example.evote.dtos.LoginAdminRequest;
import com.example.evote.services.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private static final Logger log = LoggerFactory.getLogger(AdminAuthController.class);
    
    private final AdminService adminService;
    private final JwtAdminUtil jwtAdminUtil;

    public AdminAuthController(AdminService adminService, JwtAdminUtil jwtAdminUtil) {
        this.adminService = adminService;
        this.jwtAdminUtil = jwtAdminUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginAdminRequest req) {
        try {
            log.debug("Login attempt for admin email: {}", req.email);
            
            Admin admin = adminService.authenticate(req.email, req.passwordHash);
            if (admin == null) {
                log.warn("Failed login attempt for email: {}", req.email);
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

            // Create claims map safely
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", admin.getRole().name());
            claims.put("adminId", admin.getId()); 
            
            if (admin.getDivision() != null) {
                claims.put("divisionId", admin.getDivision().getId());
            }

            String token = jwtAdminUtil.generateToken(admin.getEmail(), claims);
            
            log.info("Admin login successful for email: {}", admin.getEmail());
            
            return ResponseEntity.ok(new JwtResponse(
                token,
                admin.getRole().name(),
                admin.getId(),
                admin.getDivision() != null ? admin.getDivision().getId() : null
            ));
            
        } catch (Exception e) {
            log.error("Error during admin login", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}