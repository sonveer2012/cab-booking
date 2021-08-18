package com.sonveer.cab.sharing.entity.request;

import javax.validation.constraints.NotNull;

public abstract class RideInfo {
    @NotNull
    String origin;
    @NotNull
    String destination;
    @NotNull
    String userName;
    @NotNull
    Integer seatCount;


    public RideInfo() {
    }

    public RideInfo(@NotNull String origin,
                    @NotNull String destination,
                    @NotNull String userName, @NotNull Integer seatCount) {
        this.origin = origin;
        this.destination = destination;
        this.userName = userName;
        this.seatCount = seatCount;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }
}
