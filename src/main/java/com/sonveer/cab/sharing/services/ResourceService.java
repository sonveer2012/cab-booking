package com.sonveer.cab.sharing.services;


import com.sonveer.cab.sharing.entity.request.User;
import com.sonveer.cab.sharing.entity.request.RequestVehicleObject;
import com.sonveer.cab.sharing.errors.ResourceAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class ResourceService {
    @Autowired
    private StatService statService;

    ConcurrentHashMap<String, RequestVehicleObject> vehicleConcurrentHashMap;
    ConcurrentHashMap<String, User>                 userConcurrentHashMap;

    public ResourceService() {
        this.vehicleConcurrentHashMap = new ConcurrentHashMap<>();
        this.userConcurrentHashMap = new ConcurrentHashMap<>();
    }

    public void addUser(User user) throws ResourceAlreadyExistException {
        if (userConcurrentHashMap.containsKey(user.getName())) {
            throw new ResourceAlreadyExistException("User with given name already existing");
        }
        else {
            userConcurrentHashMap.put(user.getName(), user);
            statService.addUser(user.getName());
        }
    }

    public void addVehicle(RequestVehicleObject vehicle) throws ResourceAlreadyExistException {
        for (String vehicleNumber : vehicle.getVehicleNumber()) {
            if (vehicleConcurrentHashMap.containsKey(vehicleNumber)) {
                throw new ResourceAlreadyExistException(
                        "One or more vehicle already exsited in system , No vehicle has been added");
            }
        }
        statService.addUser(vehicle.getUserName());
        for (String vehicleNumber : vehicle.getVehicleNumber()) {
            List<String> vehicleNumberList = new ArrayList<>();
            vehicleNumberList.add(vehicleNumber);
            RequestVehicleObject vehicle1 = new RequestVehicleObject(vehicle.getUserName(), vehicle.getCabCompnany());
            vehicleConcurrentHashMap.put(vehicleNumber, vehicle1);
        }
    }

    public RequestVehicleObject getRequestedVehicleFromResource(String vehicleNumber){
        return vehicleConcurrentHashMap.get(vehicleNumber);
    }

}
