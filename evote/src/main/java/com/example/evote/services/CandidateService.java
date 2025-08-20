package com.example.evote.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evote.Entity.Candidate;
import com.example.evote.Repository.CandidateRepository;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepo;

    public CandidateService(CandidateRepository candidateRepo) {
        this.candidateRepo = candidateRepo;
    }

    public List<Candidate> list(){
        return candidateRepo.findAll();
    }

    @Transactional
    public Candidate create(String name, String party, String candidateCode){
        return candidateRepo.save(new Candidate(name, party, candidateCode));
    }
}
