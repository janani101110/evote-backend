package com.example.evote.controller.admin;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation .RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.dtos.ResultDtos;
import com.example.evote.services.ResultService;

@RestController
@RequestMapping("/api/admin/results")
public class AnalyticsController {

    private final ResultService resultService; 

    public AnalyticsController(ResultService resultService){ this.resultService = resultService; }

   @GetMapping("/election")
   @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResultDtos.ElectionResult getElectionResults() {
        return resultService.getElectionResults();
    }
}
