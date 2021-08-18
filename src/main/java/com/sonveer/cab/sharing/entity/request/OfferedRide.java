package com.sonveer.cab.sharing.entity.request;

import javax.validation.constraints.NotNull;

public class OfferedRide extends RideInfo{
    @NotNull
    String vehicleNumber;

    public OfferedRide(String origin,
                       String destination,
                       String userName,
                       Integer seatCount,
                       String vehicleNumber) {
        super(origin, destination, userName, seatCount);
        this.vehicleNumber = vehicleNumber;
    }

    public OfferedRide() {
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
