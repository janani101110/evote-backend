package com.example.evote.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.evote.Entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByCandidateCode(String candidateCode);
}
