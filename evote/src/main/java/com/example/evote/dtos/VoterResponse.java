package com.example.evote.dtos;

public class VoterResponse {
    private final long id;
    private final String nic;
    private final String fullName;
    private final String divisionId;

    public VoterResponse(Long id,String nic,String fullName, String divisionId){
        this.id = id;
        this.nic = nic;
        this.fullName = fullName;
        this.divisionId = divisionId;
    }

    public Long getId() {return id;}
    public String getnic(){return  nic;}
    public String getfullName() {return fullName;}
    public String getdivisionId() {return divisionId;}
    
}
