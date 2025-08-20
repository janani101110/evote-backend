package com.example.evote.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;
    private int preferenceOrder;

    private LocalDateTime voteTime = LocalDateTime.now();

    // Constructors
    public Vote() {}

    public Vote(User user, Candidate candidate,int preferenceOrder) {
        this.user = user;
        this.candidate = candidate;
        this.division = user.getDivision();   
        this.preferenceOrder = preferenceOrder;     
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public LocalDateTime getVoteTime() { return voteTime; }
    public void setVoteTime(LocalDateTime voteTime) { this.voteTime = voteTime; }

    public Division getDivision() {return division;}
    // public void setDivision(Division division){this.division = division;}

    public int getPreferenceOrder() { return preferenceOrder; }
    public void setPreferenceOrder(int preferenceOrder) { 
        this.preferenceOrder = preferenceOrder; 
    }
}

