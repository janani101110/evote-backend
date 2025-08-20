package com.example.evote.controller.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.evote.Entity.Admin;

public class ControllerUtil {
    public static Admin currentAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object details = auth.getDetails();
        return (details instanceof Admin) ? (Admin) details : null;
    }
}
