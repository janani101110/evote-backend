package com.example.evote.Entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "admins", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class Admin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String fullName;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String passwordHash; 

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    // Optional: limit a divisional admin to one division
    @ManyToOne
    private Division division; // null for SUPER_ADMIN

    private boolean active = true;

    public Admin() {}
    public Admin(String fullName, String email, String passwordHash, Role role, Division division) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.division = division;
    }

    // getters/setters, equals/hashCode
    // ...
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Division getDivision() { return division; }
    public void setDivision(Division division) { this.division = division; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof Admin)) return false;
        Admin a=(Admin)o;
        return Objects.equals(id,a.id);
    }
    @Override public int hashCode(){ return Objects.hash(id); }
}
