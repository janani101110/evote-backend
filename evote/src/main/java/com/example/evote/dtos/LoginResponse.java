package com.example.evote.dtos;

import com.example.evote.Entity.Division;

public class LoginResponse {
    private final Long id;
    private final String nic;
    private final String fullName;
    private final boolean hasVoted;
    private final boolean passwordSet;
    private final DivisionInfo division;

    public LoginResponse(Long id, String nic, String fullName, boolean hasVoted, boolean passwordSet, Division division) {
        this.id = id;
        this.nic = nic;
        this.fullName = fullName;
        this.hasVoted = hasVoted;
        this.passwordSet = passwordSet;
        this.division = new DivisionInfo(division.getId(), division.getDivisionName(), division.getDivisionCode());
    }

    public static class DivisionInfo {
        private final Long id;
        private final String divisionName;
        private final String divisionCode;

        public DivisionInfo(Long id, String name, String code) {
            this.id = id;
            this.divisionName = name;
            this.divisionCode = code;
        }

        public Long getId() { return id; }
        public String getDivisionName() { return divisionName; }
        public String getDivisionCode() { return divisionCode; }
    }

    public Long getId() { return id; }
    public String getNic() { return nic; }
    public String getFullName() { return fullName; }
    public boolean isHasVoted() { return hasVoted; }
    public boolean isPasswordSet() { return passwordSet; }
    public DivisionInfo getDivision() { return division; }
}
