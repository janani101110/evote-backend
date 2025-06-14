package com.example.evote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.Division;
import com.example.evote.Entity.User;
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

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (divisionRepository.count() > 0) {
            return;
        }

        // Create Divisions
        Division division1 = new Division("Colombo Central", "COL-01");
        Division division2 = new Division("Gampaha", "GAM-01");
        Division division3 = new Division("Kandy", "KAN-01");
        
        divisionRepository.save(division1);
        divisionRepository.save(division2);
        divisionRepository.save(division3);

        // Create Candidates for Colombo Central
        candidateRepository.save(new Candidate("John Silva", "Party A", division1));
        candidateRepository.save(new Candidate("Mary Fernando", "Party B", division1));
        candidateRepository.save(new Candidate("David Perera", "Independent", division1));

        // Create Candidates for Gampaha
        candidateRepository.save(new Candidate("Nimal Jayasinghe", "Party A", division2));
        candidateRepository.save(new Candidate("Saman Wickramasinghe", "Party B", division2));

        // Create Candidates for Kandy
        candidateRepository.save(new Candidate("Kamal Rajapaksa", "Party A", division3));
        candidateRepository.save(new Candidate("Sunil Bandara", "Party B", division3));

        // Create Sample Users (eligible voters)
        userRepository.save(new User("123456789V", "Amal Perera", division1));
        userRepository.save(new User("987654321V", "Nisha Silva", division1));
        userRepository.save(new User("456789123V", "Ruwan Fernando", division2));
        userRepository.save(new User("789123456V", "Sanduni Rajapaksa", division3));
        userRepository.save(new User("321654987V", "Chamara Wickrama", division2));

        System.out.println("Sample data initialized successfully!");
    }
}
