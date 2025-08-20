package com.example.evote.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

}
