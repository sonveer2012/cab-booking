package com.sonveer.cab.sharing.entity.response;

public class SelectedVehicleInfo {
    String vehicleNumber;
    String vehicleCompany;
    String riderName;
    Integer numberOfSeats;

    public SelectedVehicleInfo(String vehicleNumber, String vehicleCompany, String riderName, Integer numberOfSeats) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleCompany = vehicleCompany;
        this.riderName = riderName;
        this.numberOfSeats = numberOfSeats;
    }

    public SelectedVehicleInfo() {
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleCompany() {
        return vehicleCompany;
    }

    public void setVehicleCompany(String vehicleCompany) {
        this.vehicleCompany = vehicleCompany;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
