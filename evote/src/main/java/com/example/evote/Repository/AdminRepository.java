package com.example.evote.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evote.Entity.Admin;
import com.example.evote.Entity.Division;
import com.example.evote.Entity.Role;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Admin> findByRole(Role role); 
    List<Admin> findByDivision(Division division);
}
