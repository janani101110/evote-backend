package com.example.evote.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evote.Entity.Division;
import com.example.evote.Repository.DivisionRepository;

@Service
public class DivisionService {

    private final DivisionRepository repo;

    public DivisionService(DivisionRepository repo) {
        this.repo = repo;
    }

    public List<Division> list(){ return repo.findAll(); }

    @Transactional
    public Division create(String name, String code){
        if (repo.findByDivisionCode(code).isPresent()) throw new IllegalArgumentException("Division code already exists");
        return repo.save(new Division(name, code));
    }
}
