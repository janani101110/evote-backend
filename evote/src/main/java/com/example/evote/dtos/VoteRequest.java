package com.example.evote.dtos;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
}
