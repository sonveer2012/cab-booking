package com.sonveer.cab.sharing.services;

import com.sonveer.cab.sharing.entity.request.OfferedRide;
import com.sonveer.cab.sharing.entity.request.RequestVehicleObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static RequestVehicleObject getRequestVehicleObject() {
        List<String> vehicleNumber = new ArrayList<>();
        vehicleNumber.add("KA01-1111");
        vehicleNumber.add("KA01-1112");
        vehicleNumber.add("KA01-1113");
        return new RequestVehicleObject("TestUser", "AUDI", vehicleNumber);
    }

    public static OfferedRide getOfferedRideObjectWith2Seat(String vehicleNumber) {
        return new OfferedRide("goa", "delhu", "testUser", 2, vehicleNumber);
    }




}
