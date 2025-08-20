package com.example.evote.dtos;

import java.util.List;

public class ResultDtos {

    public static class CountItem {
        public Long id; public String name; public long count;
        public CountItem(Long id, String name, long count){ this.id=id; this.name=name; this.count=count; }
    }
 
    public static class OverallResult { 
        public long totalVotes;
        public List<CountItem> byCandidate;
        public List<CountItem> byDivision;
        public OverallResult(long totalVotes, List<CountItem> byCandidate, List<CountItem> byDivision) {
            this.totalVotes = totalVotes; this.byCandidate = byCandidate; this.byDivision = byDivision;
        }
    }
    public static class PreferenceResult {
        public List<CountItem> firstPreferences;
        public List<CountItem> secondPreferences;
        public Long winnerId;
        
        public PreferenceResult(List<CountItem> firstPreferences, 
                              List<CountItem> secondPreferences,
                              Long winnerId) {
            this.firstPreferences = firstPreferences;
            this.secondPreferences = secondPreferences;
            this.winnerId = winnerId;
        }
    }
    
    public static class DivisionResult {
        public Long divisionId;
        public String divisionName;
        public PreferenceResult candidateResults;
        
        public DivisionResult(Long divisionId, String divisionName, 
                            PreferenceResult candidateResults) {
            this.divisionId = divisionId;
            this.divisionName = divisionName;
            this.candidateResults = candidateResults;
        }
    }
    
    public static class ElectionResult {
        public PreferenceResult overallResult;
        public List<DivisionResult> divisionResults;
        
        public ElectionResult(PreferenceResult overallResult, 
                            List<DivisionResult> divisionResults) {
            this.overallResult = overallResult;
            this.divisionResults = divisionResults;
        }
    }
}