package com.example.evote.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VoterDtos {
    public static class CreateVoterRequest {
        @NotBlank public String nic;
        @NotBlank public String fullName;
        @NotNull public Long divisionId;
    }
}