package com.sonveer.cab.sharing.entity.request;

import javax.validation.constraints.NotNull;

public class RequestRide extends RideInfo{
    @NotNull
    RideEnum preferredCat;
    String company;
    public RequestRide(String origin,
                       String destination,
                       String userName,
                       Integer seatCount,
                       RideEnum preferredCat) {
        super(origin, destination, userName, seatCount);
        this.preferredCat = preferredCat;
    }

    public RequestRide(RideEnum preferredCat) {
        this.preferredCat = preferredCat;
    }

    public RequestRide() {
        super();
    }

    public RequestRide(String origin,
                       String destination,
                       String userName,
                       Integer seatCount,
                       @NotNull RideEnum preferredCat, String company) {
        super(origin, destination, userName, seatCount);
        this.preferredCat = preferredCat;
        this.company = company;
    }

    public RequestRide(@NotNull RideEnum preferredCat, String company) {
        this.preferredCat = preferredCat;
        this.company = company;
    }

    public RideEnum getPreferredCat() {
        return preferredCat;
    }

    public void setPreferredCat(RideEnum preferredCat) {
        this.preferredCat = preferredCat;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


}
