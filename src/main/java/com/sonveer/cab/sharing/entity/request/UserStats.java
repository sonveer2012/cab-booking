package com.sonveer.cab.sharing.entity.request;

public class UserStats {
    Integer offeredRide;
    Integer rideTaken;
    public UserStats(){
        this.offeredRide = 0;
        this.rideTaken = 0;
    }

    public void increaseOfferRide(){
        this.offeredRide = this.offeredRide + 1;
    }

    public void increaseTakenRide(){
        this.rideTaken = this.rideTaken + 1;
    }

    public Integer getOfferedRide() {
        return offeredRide;
    }

    public Integer getRideTaken() {
        return rideTaken;
    }
}
