package com.example.evote.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.Division;
import com.example.evote.Entity.Vote;
 
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserId(Long userId);
    
    long countByDivision(Division division); 
    
    long countByCandidate(Candidate candidate);

     @Query("SELECT v.candidate.id, COUNT(v) FROM Vote v WHERE v.preferenceOrder = 1 GROUP BY v.candidate.id")
    List<Object[]> countFirstPreferencesByCandidate();
    
    // Count 2nd preferences by candidate
    @Query("SELECT v.candidate.id, COUNT(v) FROM Vote v WHERE v.preferenceOrder = 2 GROUP BY v.candidate.id")
    List<Object[]> countSecondPreferencesByCandidate();
    
    // Count 1st preferences by candidate and division
    @Query("""
        SELECT v.candidate.id, v.division.id, COUNT(v) 
        FROM Vote v 
        WHERE v.preferenceOrder = 1
        GROUP BY v.candidate.id, v.division.id
        """)
    List<Object[]> countFirstPreferencesByCandidateAndDivision();
    
    // Count 2nd preferences by candidate and division
    @Query("""
        SELECT v.candidate.id, v.division.id, COUNT(v) 
        FROM Vote v 
        WHERE v.preferenceOrder = 2
        GROUP BY v.candidate.id, v.division.id
        """)
    List<Object[]> countSecondPreferencesByCandidateAndDivision();
}