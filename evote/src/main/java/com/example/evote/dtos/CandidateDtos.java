package com.example.evote.dtos;


import jakarta.validation.constraints.NotBlank;

public class CandidateDtos {
    public static class CreateCandidateRequest {
        @NotBlank public String name;
        @NotBlank public String party;
        @NotBlank public String candidateCode;
    }
}