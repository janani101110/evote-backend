package com.example.evote.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "divisions")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String divisionName;

    @Column(nullable = false)
    private String divisionCode;

    @OneToMany(mappedBy = "division")
    private List<User> voters;

    @OneToMany(mappedBy = "division")
    private List<Candidate> candidates;

    // Constructors
    public Division() {}

    public Division(String divisionName, String divisionCode) {
        this.divisionName = divisionName;
        this.divisionCode = divisionCode;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public String getDivisionCode() { return divisionCode; }
    public void setDivisionCode(String divisionCode) { this.divisionCode = divisionCode; }

    public List<User> getVoters() { return voters; }
    public void setVoters(List<User> voters) { this.voters = voters; }

    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
}
