package com.example.evote.services;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evote.Entity.Candidate;
import com.example.evote.Entity.User;
import com.example.evote.Entity.Vote;
import com.example.evote.Repository.CandidateRepository;
import com.example.evote.Repository.UserRepository;
import com.example.evote.Repository.VoteRepository;

@Service
@Transactional
public class VotingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User validateNic(String nic) {
        Optional<User> user = userRepository.findByNic(nic);
        if (user.isPresent() && user.get().isEligible()) {
            return user.get();
        }
        return null;
    }

    public boolean setPassword(String nic, String password) {
        Optional<User> userOpt = userRepository.findByNic(nic);
        if (userOpt.isPresent() && !userOpt.get().isPasswordSet()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordSet(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User authenticate(String nic, String password) {
        Optional<User> userOpt = userRepository.findByNic(nic);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isPasswordSet() && passwordEncoder.matches(password, user.getPassword())) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }

   public List<Candidate> getAllCandidates(){
        return candidateRepository.findAll();
    }

    public boolean castVotes(Long userId, List<Long> candidateIds) {
    Optional<User> userOpt = userRepository.findById(userId);

    if (userOpt.isPresent()) {
        User user = userOpt.get();

        // Check if already voted
        if (user.isHasVoted() || voteRepository.existsByUserId(userId)) {
            return false;
        }

        for (int i = 0; i < candidateIds.size(); i++) {
        Candidate candidate = candidateRepository.findById(candidateIds.get(i)).orElse(null);
        if (candidate != null) {
            Vote vote = new Vote(user, candidate, i + 1); // i+1 converts 0-based index to 1-based preference
            voteRepository.save(vote);
        }
    }

        // Mark user as voted
        user.setHasVoted(true);
        userRepository.save(user);

        return true;
    }
    return false;
}

}
