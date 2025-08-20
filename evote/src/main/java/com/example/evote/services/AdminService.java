package com.example.evote.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evote.Entity.Admin;
import com.example.evote.Entity.Division;
import com.example.evote.Entity.Role;
import com.example.evote.Repository.AdminRepository;
import com.example.evote.Repository.DivisionRepository;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    
    private final AdminRepository adminRepo;
    private final DivisionRepository divisionRepo;
    private final PasswordEncoder encoder;

    public AdminService(AdminRepository adminRepo, DivisionRepository divisionRepo, PasswordEncoder encoder) {
        this.adminRepo = adminRepo;
        this.divisionRepo = divisionRepo;
        this.encoder = encoder;
    }

    public Admin authenticate(String email, String password) {
        log.debug("Attempting to authenticate admin with email: {}", email);
        Admin admin = adminRepo.findByEmail(email).orElse(null);
        
        if (admin == null) {
            log.debug("Admin not found with email: {}", email);
            return null;
        }
        
        if (!admin.isActive()) {
            log.debug("Admin found but inactive: {}", email);
            return null;
        }
        
        if (!encoder.matches(password, admin.getPasswordHash())) {
            log.debug("Password mismatch for admin: {}", email);
            return null;
        }
        
        log.info("Admin authenticated successfully: {}", email);
        return admin;
    }

    @Transactional
    public Admin createAdmin(String fullName, String email, String rawPassword, Role role, Long divisionId) {
        log.debug("Creating new admin with email: {}", email);
        if (adminRepo.existsByEmail(email)) {
            log.warn("Attempt to create admin with existing email: {}", email);
            throw new IllegalArgumentException("Email already used");
        }
        
        Division division = null;
        if (role == Role.DIVISIONAL_ADMIN) { 
            if (divisionId == null) {
                log.error("Division ID required for divisional admin");
                throw new IllegalArgumentException("Division is required for divisional admins");
            }
            division = divisionRepo.findById(divisionId)
                .orElseThrow(() -> {
                    log.error("Division not found with ID: {}", divisionId);
                    return new IllegalArgumentException("Division not found");
                });
        }
        
        Admin a = new Admin(fullName, email, encoder.encode(rawPassword), role, division);
        Admin savedAdmin = adminRepo.save(a);
        log.info("Created new admin with ID: {}", savedAdmin.getId());
        return savedAdmin;
    }

    public List<Admin> listAdmins() {
        log.debug("Fetching all admins");
        return adminRepo.findAll();
    }

    public void deactivate(Long id) {
        log.debug("Deactivating admin with ID: {}", id);
        Admin a = adminRepo.findById(id)
            .orElseThrow(() -> {
                log.error("Admin not found with ID: {}", id);
                return new IllegalArgumentException("Admin not found");
            });
        a.setActive(false);
        adminRepo.save(a);
        log.info("Admin deactivated with ID: {}", id);
    }
}