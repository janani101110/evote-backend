package com.example.evote.controller.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.Candidate;
import com.example.evote.dtos.CandidateDtos.CreateCandidateRequest;
import com.example.evote.dtos.CandidateResponse;
import com.example.evote.services.CandidateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/candidates")
public class CandidateController { 

    private final CandidateService service;
    public CandidateController(CandidateService service){ this.service=service; }

    @GetMapping
    public List<CandidateResponse> list() {
        return service.list().stream()
            .map(c -> new CandidateResponse(
                c.getId(),
                c.getCandidateName(),
                c.getPartyName(),
                c.getCandidateCode()
            ))
            .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Candidate create(@Valid @RequestBody CreateCandidateRequest req){
        return service.create(req.name, req.party, req.candidateCode);
    }
}
