package com.example.evote.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String candidateName;

    private String partyName;
    private String symbol;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;

    private boolean isActive = true;

    // Constructors
    public Candidate() {}

    public Candidate(String candidateName, String partyName, Division division) {
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.division = division;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public Division getDivision() { return division; }
    public void setDivision(Division division) { this.division = division; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}

