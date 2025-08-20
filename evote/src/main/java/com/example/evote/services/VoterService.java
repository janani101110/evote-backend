package com.example.evote.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evote.Entity.Division;
import com.example.evote.Entity.User;
import com.example.evote.Repository.DivisionRepository;
import com.example.evote.Repository.UserRepository;

@Service
public class VoterService {

    private final UserRepository userRepo;
    private final DivisionRepository divisionRepo;

    public VoterService(UserRepository userRepo, DivisionRepository divisionRepo) {
        this.userRepo = userRepo;
        this.divisionRepo = divisionRepo;
    }

    public List<User> list(){ return userRepo.findAll(); }

    @Transactional
    public User create(String nic, String fullName, Long divisionId){
        Division d = divisionRepo.findById(divisionId).orElseThrow();
        return userRepo.save(new User(nic, fullName, d));
    }
}
