package com.example.evote.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.Division;
import com.example.evote.Repository.CandidateRepository;
import com.example.evote.Repository.DivisionRepository;
import com.example.evote.Repository.VoteRepository;
import com.example.evote.dtos.ResultDtos;



@Service
public class ResultService {

    private final VoteRepository voteRepo;
    private final CandidateRepository candidateRepo;
    private final DivisionRepository divisionRepo;

    public ResultService(VoteRepository voteRepo, CandidateRepository candidateRepo, DivisionRepository divisionRepo) {
        this.voteRepo = voteRepo;
        this.candidateRepo = candidateRepo; 
        this.divisionRepo = divisionRepo;
    }

   
 public ResultDtos.ElectionResult getElectionResults() {
        // Get all candidate and division names
        Map<Long, String> candidateNames = getCandidateNames();
        Map<Long, String> divisionNames = getDivisionNames();
        
        // Process overall results
        ResultDtos.PreferenceResult overall = getPreferenceResults(
            voteRepo.countFirstPreferencesByCandidate(),
            voteRepo.countSecondPreferencesByCandidate(),
            candidateNames
        );
        
        // Process division-wise results
        List<ResultDtos.DivisionResult> divisionResults = new ArrayList<>();
        
        // Group by division first
        Map<Long, List<Object[]>> firstPrefByDivision = voteRepo.countFirstPreferencesByCandidateAndDivision()
            .stream()
            .collect(Collectors.groupingBy(arr -> ((Number) arr[1]).longValue()));
            
        Map<Long, List<Object[]>> secondPrefByDivision = voteRepo.countSecondPreferencesByCandidateAndDivision()
            .stream()
            .collect(Collectors.groupingBy(arr -> ((Number) arr[1]).longValue()));
            
        for (Long divisionId : divisionNames.keySet()) {
            List<Object[]> firstPref = firstPrefByDivision.getOrDefault(divisionId, Collections.emptyList());
            List<Object[]> secondPref = secondPrefByDivision.getOrDefault(divisionId, Collections.emptyList());
            
            ResultDtos.PreferenceResult divResult = getPreferenceResults(
                firstPref,
                secondPref,
                candidateNames
            );
            
            divisionResults.add(new ResultDtos.DivisionResult(
                divisionId,
                divisionNames.get(divisionId),
                divResult
            ));
        }
        
        return new ResultDtos.ElectionResult(overall, divisionResults);
    }
    
    private ResultDtos.PreferenceResult getPreferenceResults(
            List<Object[]> firstPrefData,
            List<Object[]> secondPrefData,
            Map<Long, String> candidateNames) {
        
        // Process first preferences
        List<ResultDtos.CountItem> firstPref = firstPrefData.stream()
            .filter(arr -> arr != null && arr.length >= 2 && arr[0] != null)
            .map(arr -> new ResultDtos.CountItem(
                ((Number) arr[0]).longValue(),
                candidateNames.getOrDefault(((Number) arr[0]).longValue(), "Unknown"),
                ((Number) arr[1]).longValue()
            ))
            .sorted((a, b) -> Long.compare(b.count, a.count))
            .collect(Collectors.toList());
            
        // Process second preferences
        List<ResultDtos.CountItem> secondPref = secondPrefData.stream()
            .filter(arr -> arr != null && arr.length >= 2 && arr[0] != null)
            .map(arr -> new ResultDtos.CountItem(
                ((Number) arr[0]).longValue(),
                candidateNames.getOrDefault(((Number) arr[0]).longValue(), "Unknown"),
                ((Number) arr[1]).longValue()
            ))
            .sorted((a, b) -> Long.compare(b.count, a.count))
            .collect(Collectors.toList());
            
        // Determine winner (considering second preferences if first preferences are tied)
        Long winnerId = determineWinner(firstPref, secondPref);
        
        return new ResultDtos.PreferenceResult(firstPref, secondPref, winnerId);
    }
    
    private Long determineWinner(List<ResultDtos.CountItem> firstPref, 
                               List<ResultDtos.CountItem> secondPref) {
        if (firstPref.isEmpty()) return null;
        
        // Find max votes in first preferences
        long maxVotes = firstPref.get(0).count;
        List<ResultDtos.CountItem> leadingCandidates = firstPref.stream()
            .filter(c -> c.count == maxVotes)
            .collect(Collectors.toList());
            
        if (leadingCandidates.size() == 1) {
            return leadingCandidates.get(0).id;
        } else {
            // Tie - use second preferences
            Map<Long, Long> secondPrefMap = secondPref.stream()
                .collect(Collectors.toMap(
                    c -> c.id,
                    c -> c.count,
                    (a, b) -> a
                ));
                
            return leadingCandidates.stream()
                .max(Comparator.comparingLong(c -> secondPrefMap.getOrDefault(c.id, 0L)))
                .map(c -> c.id)
                .orElse(null);
        }
    }
   

    @Cacheable(value = "candidateNames")
    public Map<Long, String> getCandidateNames() {
        return candidateRepo.findAll()
                .stream()
                .collect(Collectors.toMap(
                    Candidate::getId,
                    Candidate::getCandidateName,
                    (existing, replacement) -> existing,
                    HashMap::new
                ));
    }

    @Cacheable(value = "divisionNames")
    public Map<Long, String> getDivisionNames() {
        return divisionRepo.findAll()
                .stream()
                .collect(Collectors.toMap(
                    Division::getId,
                    Division::getDivisionName,
                    (existing, replacement) -> existing,
                    HashMap::new
                ));
    }
}