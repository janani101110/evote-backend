package com.example.evote.dtos;

import jakarta.validation.constraints.NotBlank;

public class DivisionDtos {
    public static class CreateDivisionRequest {
        @NotBlank public String name;
        @NotBlank public String code;
    }
}