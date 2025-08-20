package com.example.evote.controller.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.Division;
import com.example.evote.dtos.DivisionDtos.CreateDivisionRequest;
import com.example.evote.dtos.DivisionResponse;
import com.example.evote.services.DivisionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/divisions")
public class DivisionController {

    private final DivisionService service;

    public DivisionController(DivisionService service) {
        this.service = service;
    }

    @GetMapping
    public List<DivisionResponse> list() {
        return service.list().stream()
            .map(d -> new DivisionResponse(
                d.getId(),
                d.getDivisionName(),
                d.getDivisionCode()
            ))
            .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Division create(@Valid @RequestBody CreateDivisionRequest req) {
        return service.create(req.name, req.code);
    }
}
