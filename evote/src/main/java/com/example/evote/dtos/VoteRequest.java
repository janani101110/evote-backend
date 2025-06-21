package com.example.evote.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class VoteRequest {

    @NotEmpty(message = "At least one candidate must be selected")
    private List<Long> candidateIds;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "User division is required")
    private String userDivision;

    // Getters and Setters
    public List<Long> getCandidateIds() {
        return candidateIds;
    }

    public void setCandidateIds(List<Long> candidateIds) {
        this.candidateIds = candidateIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserDivision() {
        return userDivision;
    }

    public void setUserDivision(String userDivision) {
        this.userDivision = userDivision;
    }
}
