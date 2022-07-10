package com.example.flighthome;

public class Flight {
    private String leaveTime;
    private String arriveTime;
    private String airline;
    private String flightNum;
    private String fromIata;
    private String toIata;

    public Flight(String leaveTime,String arriveTime,String airline,String flightNum,String fromIata,String toIata){
        this.leaveTime=leaveTime;
        this.arriveTime=arriveTime;
        this.airline=airline;
        this.fromIata=fromIata;
        this.toIata=toIata;
        this.flightNum=flightNum;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public void setFromIata(String fromIata) {
        this.fromIata = fromIata;
    }

    public void setToIata(String toIata) {
        this.toIata = toIata;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public String getFromIata() {
        return fromIata;
    }

    public String getToIata() {
        return toIata;
    }
}
