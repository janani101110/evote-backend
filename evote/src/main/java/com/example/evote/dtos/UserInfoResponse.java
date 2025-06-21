package com.example.evote.dtos;

import com.example.evote.Entity.User;

public class UserInfoResponse {
    final private Long id;
    final private String nic;
    final private String fullName;
    final private boolean hasVoted;
    final private boolean passwordSet;

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.nic = user.getNic();
        this.fullName = user.getFullName();
        this.hasVoted = user.isHasVoted();
        this.passwordSet = user.isPasswordSet();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getNic() {
        return nic;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

}