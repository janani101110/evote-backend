package com.example.evote.dtos;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "NIC is required")
    private String nic;

    @NotBlank(message = "Password is required")
    private String password;

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

