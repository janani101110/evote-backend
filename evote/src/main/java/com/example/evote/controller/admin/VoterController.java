package com.example.evote.controller.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.User;
import com.example.evote.dtos.VoterDtos.CreateVoterRequest;
import com.example.evote.dtos.VoterResponse;
import com.example.evote.services.VoterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/voters")
public class VoterController {

    private final VoterService service;
    public VoterController(VoterService service){ this.service=service; }

    @GetMapping
    public List<VoterResponse> list(){ return service.list().stream()
    .map(v -> new VoterResponse(
        v.getId(),
        v.getNic(),
        v.getFullName(),
        v.getDivision().getDivisionCode()
    )).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public User create(@Valid @RequestBody CreateVoterRequest req){
        return service.create(req.nic, req.fullName, req.divisionId);
    }
}
