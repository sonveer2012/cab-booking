package com.sonveer.cab.sharing.entity.request;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

public class RequestVehicleObject {
    @NotNull
    String userName; // Mapping we'll not store
    @NotNull
    String       cabCompnany;
    @ElementCollection
    List<String> vehicleNumber;

    public RequestVehicleObject(String userName, String cabCompnany, @NotNull List<String> vehicleNumber) {
        this.userName = userName;
        this.cabCompnany = cabCompnany;
        this.vehicleNumber = vehicleNumber;
    }

    public RequestVehicleObject() {

    }

    public RequestVehicleObject(@NotNull String userName,
                                @NotNull String cabCompnany) {
        this.userName = userName;
        this.cabCompnany = cabCompnany;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCabCompnany() {
        return cabCompnany;
    }

    public void setCabCompnany(String cabCompnany) {
        this.cabCompnany = cabCompnany;
    }

    public List<String> getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(List<String> vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
