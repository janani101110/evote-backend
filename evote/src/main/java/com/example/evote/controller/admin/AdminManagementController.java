package com.example.evote.controller.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.Admin;
import com.example.evote.dtos.AdminDtos.AdminResponse;
import com.example.evote.dtos.AdminDtos.CreateAdminRequest;
import com.example.evote.dtos.VoteRequest;
import com.example.evote.services.AdminService;
import com.example.evote.services.VotingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/super/admins")
public class AdminManagementController {

    private final VotingService votingService;

    private final AdminService adminService;

    public AdminManagementController(AdminService adminService,VotingService votingService) {
        this.adminService = adminService;
        this.votingService = votingService;
    };

    // ONLY SUPER ADMIN
    @PostMapping 
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public AdminResponse create(@Valid @RequestBody CreateAdminRequest req){
        Admin a = adminService.createAdmin(req.fullName, req.email, req.password, req.role, req.divisionId);
        return new AdminResponse(a.getId(), a.getFullName(), a.getEmail(), a.getRole().name(),
                a.getDivision()==null?null:a.getDivision().getId(), a.isActive());
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<AdminResponse> list(){
        return adminService.listAdmins().stream()
                .map(a -> new AdminResponse(a.getId(), a.getFullName(), a.getEmail(), a.getRole().name(),
                        a.getDivision()==null?null:a.getDivision().getId(), a.isActive()))
                .toList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deactivate(@PathVariable Long id){
        adminService.deactivate(id);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void activate(@PathVariable Long id){
        adminService.activate(id);
    }

@PostMapping("/vote/by-admin")
@PreAuthorize("hasRole('DIVISIONAL_ADMIN')")
public boolean castVotesByAdmin(@RequestBody VoteRequest request) {
    return votingService.castVotes(request.getUserId(), request.getCandidateIds());
}

}
