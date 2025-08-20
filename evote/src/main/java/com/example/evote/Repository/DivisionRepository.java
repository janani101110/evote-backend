package com.example.evote.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.evote.Entity.Division;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    Optional<Division> findByDivisionCode(String code);
}

