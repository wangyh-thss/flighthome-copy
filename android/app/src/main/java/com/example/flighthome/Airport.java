package com.example.flighthome;

public class Airport {
    private String name;
    private String iata;

    public Airport(String name,String iata){
        this.name = name;
        this.iata = iata;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getName() {
        return name;
    }

    public String getIata() {
        return iata;
    }
}
