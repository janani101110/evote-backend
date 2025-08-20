package com.example.evote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.evote.Entity.Admin;
import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.Division;
import com.example.evote.Entity.Role;
import com.example.evote.Entity.User;
import com.example.evote.Repository.AdminRepository;
import com.example.evote.Repository.CandidateRepository;
import com.example.evote.Repository.DivisionRepository;
import com.example.evote.Repository.UserRepository;

@Component

public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DivisionRepository divisionRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired 
    private AdminRepository adminRepository;
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (divisionRepository.count() == 0) {
            // Create Divisions
            Division division1 = new Division("Colombo Central", "COL-01");
            Division division2 = new Division("Gampaha", "GAM-01");
            Division division3 = new Division("Kandy", "KAN-01");
            
            divisionRepository.save(division1);
            divisionRepository.save(division2);
            divisionRepository.save(division3);

            // Create Candidates
            candidateRepository.save(new Candidate("John Silva", "Party A","A_01"));
            candidateRepository.save(new Candidate("Mary Fernando", "Party B","B_01"));
            candidateRepository.save(new Candidate("David Perera", "Independent","Indi_01"));
            candidateRepository.save(new Candidate("Nimal Jayasinghe", "Party A","A_02"));
            candidateRepository.save(new Candidate("Saman Wickramasinghe", "Party B","B_02"));
            candidateRepository.save(new Candidate("Kamal Rajapaksa", "Party A","A_03"));
            candidateRepository.save(new Candidate("Sunil Bandara", "Party B","B_03"));

            // Create Users
            userRepository.save(new User("123456789V", "Amal Perera", division1));
            userRepository.save(new User("987654321V", "Nisha Silva", division1));
            userRepository.save(new User("456789123V", "Ruwan Fernando", division2));
            userRepository.save(new User("789123456V", "Sanduni Rajapaksa", division3));
            userRepository.save(new User("321654987V", "Chamara Wickrama", division2));

            System.out.println("Sample data initialized successfully!");
        }

        // Always check/create admin if it doesn't exist
        if (adminRepository.count() == 0) {
            Admin superAdmin = new Admin(
                "System Super",
                "super@evote.local",
                passwordEncoder.encode("Super1234!"),
                Role.SUPER_ADMIN,
                null
            );
            adminRepository.save(superAdmin);
            System.out.println("Super admin created successfully!");
        }
    }
}