package com.example.evote.dtos;

public class DivisionResponse {
    private final long id;
    private final String divisionName;
    private final String divisionId;

    public DivisionResponse(Long id, String divisionName, String divisionId){
        this.id = id;
        this.divisionName = divisionName;
        this.divisionId = divisionId;
    }

    public Long getId() {return id;}
    public String getdivisionName() { return divisionName;}
    public String getdivisionId() {return divisionId;}
}
