package com.example.evote.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String candidateName;
    private String candidateCode;
    private String partyName;
    

    

    private boolean isActive = true;

    // Constructors
    public Candidate() {}

    public Candidate(String candidateName, String partyName, String candidateCode) {
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.candidateCode = candidateCode;
        
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getCandidateCode() {return candidateCode;}
    public void setCandidateCode(String candidateCode) {this.candidateCode = candidateCode;}

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}

