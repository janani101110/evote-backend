package com.example.evote.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NicValidationRequest {
    @NotBlank(message = "NIC is required")
    @Size(min = 10, max = 12, message = "NIC must be 10-12 characters")
    private String nic;

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
}
