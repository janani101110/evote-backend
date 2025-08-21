package com.example.evote.dtos;

public class DivisionResponse {
    private final long id;
    private final String divisionName;
    private final String divisionCode;

    public DivisionResponse(Long id, String divisionName, String divisionCode){
        this.id = id;
        this.divisionName = divisionName;
        this.divisionCode = divisionCode;
    }

    public Long getId() {return id;}
    public String getdivisionName() { return divisionName;}
    public String getdivisionCode() {return divisionCode;}
}
