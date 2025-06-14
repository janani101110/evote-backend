package com.example.evote.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.User;
import com.example.evote.Util.JwtUtil;
import com.example.evote.dtos.ApiResponse;
import com.example.evote.dtos.CandidateResponse;
import com.example.evote.dtos.LoginRequest;
import com.example.evote.dtos.LoginResponse;
import com.example.evote.dtos.NicValidationRequest;
import com.example.evote.dtos.SetPasswordRequest;
import com.example.evote.dtos.VoteRequest;
import com.example.evote.services.VotingService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/voting")
@CrossOrigin(origins = "*")
public class VotingController {

    @Autowired
    private VotingService votingService;
    @Autowired
private JwtUtil jwtUtil;

    // Step 1: Validate NIC
    @PostMapping("/validate-nic")
    public ResponseEntity<ApiResponse<User>> validateNic(@Valid @RequestBody NicValidationRequest request) {
        User user = votingService.validateNic(request.getNic());
        if (user != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "NIC is valid", user));
        }
        return ResponseEntity.ok(new ApiResponse<>(false, "NIC is not eligible for voting"));
    }

    // Step 2: Set Password
    @PostMapping("/set-password")
    public ResponseEntity<ApiResponse<String>> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        boolean success = votingService.setPassword(request.getNic(), request.getPassword());
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Password set successfully"));
        }
        return ResponseEntity.ok(new ApiResponse<>(false, "Failed to set password"));
    }

    // Step 3: Login
//     @PostMapping("/login")
// public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
//     User user = votingService.authenticate(request.getNic(), request.getPassword());

//     if (user != null) {
//         session.setAttribute("userId", user.getId());
//         session.setAttribute("userNic", user.getNic());

//         LoginResponse response = new LoginResponse(
//             user.getId(),
//             user.getNic(),
//             user.getFullName(),
//             user.isHasVoted(),
//             user.isPasswordSet(),
//             user.getDivision()
//         );

//         return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
//     }

//     return ResponseEntity.ok(new ApiResponse<>(false, "Invalid credentials"));
// }
@PostMapping("/login")
public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
    User user = votingService.authenticate(request.getNic(), request.getPassword());
    if (user != null) {
        String token = jwtUtil.generateToken(user.getNic());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", new LoginResponse( // assuming you use a DTO
            user.getId(), user.getNic(), user.getFullName(), 
            user.isHasVoted(), user.isPasswordSet(), user.getDivision()
        ));

        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    return ResponseEntity.ok(new ApiResponse<>(false, "Invalid credentials"));
}



    // Step 4: Get Division Info (already in user object from login)
    
    // Step 5: Get Candidates
    // @GetMapping("/candidates")
    // public ResponseEntity<ApiResponse<List<Candidate>>> getCandidates(HttpSession session) {
    //     Long userId = (Long) session.getAttribute("userId");
    //     if (userId == null) {
    //         return ResponseEntity.ok(new ApiResponse<>(false, "Please login first"));
    //     }

    //     // Get user's division and fetch candidates
    //     User user = votingService.validateNic((String) session.getAttribute("userNic"));
    //     if (user != null && user.getDivision() != null) {
    //         List<Candidate> candidates = votingService.getCandidatesByDivision(user.getDivision().getId());
    //         return ResponseEntity.ok(new ApiResponse<>(true, "Candidates retrieved", candidates));
    //     }
    //     return ResponseEntity.ok(new ApiResponse<>(false, "Unable to fetch candidates"));
    // }
@GetMapping("/candidates")
public ResponseEntity<ApiResponse<List<CandidateResponse>>> getCandidates(
        @RequestHeader("Authorization") String authHeader) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.ok(new ApiResponse<>(false, "Missing or invalid token"));
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
        return ResponseEntity.ok(new ApiResponse<>(false, "Invalid token"));
    }

    String nic = jwtUtil.extractNic(token);
    User user = votingService.validateNic(nic);

    if (user != null && user.getDivision() != null) {
        List<Candidate> candidates = votingService.getCandidatesByDivision(user.getDivision().getId());

        List<CandidateResponse> dtoList = candidates.stream().map(candidate ->
            new CandidateResponse(
                candidate.getId(),
                candidate.getCandidateName(),
                candidate.getPartyName(),
                candidate.getSymbol(),
                candidate.getPhotoUrl(),
                candidate.getDivision().getDivisionName()
            )
        ).toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Candidates retrieved", dtoList));
    }

    return ResponseEntity.ok(new ApiResponse<>(false, "Unable to fetch candidates"));
}


    // Step 6: Cast Vote
    @PostMapping("/vote")
public ResponseEntity<ApiResponse<String>> castVote(
        @Valid @RequestBody VoteRequest request,
        @RequestHeader("Authorization") String authHeader) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "Invalid or expired token"));
    }

    // Extract NIC from token, find the user, then cast vote
    String nic = jwtUtil.extractNic(token);
    User user = votingService.validateNic(nic);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, "User not found"));
    }

    boolean success = votingService.castVote(user.getId(), request.getCandidateId());
    if (success) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Vote cast successfully"));
    } else {
        return ResponseEntity.ok(new ApiResponse<>(false, "Failed to cast vote or already voted"));
    }
}


    // Step 7: Logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully"));
    }
}
