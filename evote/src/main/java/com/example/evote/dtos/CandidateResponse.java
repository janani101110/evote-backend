package com.example.evote.dtos;

public class CandidateResponse {
    private final Long id;
    private final String candidateName;
    private final String partyName;
    private final String candidateCode;
    
    

    public CandidateResponse(Long id, String candidateName, String partyName, String candidateCode) {
        this.id = id;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.candidateCode = candidateCode;
        
    }

    // Getters
    public Long getId() { return id; }
    public String getCandidateName() { return candidateName; }
    public String getPartyName() { return partyName; }
    public String getCandidateCode() {return candidateCode;}
}

