package com.example.evote.dtos;

public class CandidateResponse {
    private final Long id;
    private final String candidateName;
    private final String partyName;
    private final String symbol;
    private final String photoUrl;
    private final String divisionName;

    public CandidateResponse(Long id, String candidateName, String partyName, String symbol, String photoUrl, String divisionName) {
        this.id = id;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.symbol = symbol;
        this.photoUrl = photoUrl;
        this.divisionName = divisionName;
    }

    // Getters
    public Long getId() { return id; }
    public String getCandidateName() { return candidateName; }
    public String getPartyName() { return partyName; }
    public String getSymbol() { return symbol; }
    public String getPhotoUrl() { return photoUrl; }
    public String getDivisionName() { return divisionName; }
}

