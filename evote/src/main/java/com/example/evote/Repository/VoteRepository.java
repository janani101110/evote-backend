package com.example.evote.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.evote.Entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserId(Long userId);
}
