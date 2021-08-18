package com.sonveer.cab.sharing.controller;

import com.sonveer.cab.sharing.entity.request.OfferedRide;
import com.sonveer.cab.sharing.entity.request.RequestRide;
import com.sonveer.cab.sharing.entity.request.RequestVehicleObject;
import com.sonveer.cab.sharing.entity.request.User;
import com.sonveer.cab.sharing.entity.request.UserStats;
import com.sonveer.cab.sharing.entity.response.ResponseMessage;
import com.sonveer.cab.sharing.entity.response.SelectResponse;
import com.sonveer.cab.sharing.entity.response.SelectedVehicleInfo;
import com.sonveer.cab.sharing.errors.BadRequest;
import com.sonveer.cab.sharing.errors.AlreadyInActiveBookingException;
import com.sonveer.cab.sharing.errors.ResourceAlreadyExistException;
import com.sonveer.cab.sharing.errors.ResourceNotFoundException;
import com.sonveer.cab.sharing.services.ResourceService;
import com.sonveer.cab.sharing.services.RideService;
import com.sonveer.cab.sharing.services.StatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1")
public class Controller {

    @Autowired
    private StatService     statService;
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RideService rideService;

    @PutMapping("/add-user")
    public ResponseEntity<ResponseMessage> addUser(@Valid @RequestBody User user) {
        ResponseMessage responseMessage;
        int statusCode;
        String message;
        try {
            resourceService.addUser(user);
            message = "User has been added";
            statusCode = 201;
        }
        catch (ResourceAlreadyExistException resourceAlreadyExistException) {
            message = resourceAlreadyExistException.getMessage();
            statusCode = 405;
        }
        return ResponseEntity.status(statusCode).body(new ResponseMessage(message));
    }

    @PutMapping("/add-vehicle")
    public ResponseEntity<ResponseMessage> addVehicle(@Valid @RequestBody RequestVehicleObject vehicle) {
        ResponseMessage responseMessage;
        int statusCode;
        String message;
        try {
            resourceService.addVehicle(vehicle);
            statusCode = 201;
            message = "Vehicle has been added";
        }
        catch (ResourceAlreadyExistException resourceAlreadyExistException) {
            statusCode = 405;
            message = resourceAlreadyExistException.getMessage();
        }
        return ResponseEntity.status(statusCode).body(new ResponseMessage(message));
    }

    @PostMapping("/offer-ride")
    public ResponseEntity<ResponseMessage> offerRide(@Valid @RequestBody OfferedRide OfferedRide) {
        String message;
        int statusCode;
        try {
            rideService.offerRide(OfferedRide);
            message = "Status has been updated to offered";
            statusCode = 201;
        }
        catch (AlreadyInActiveBookingException e) {
            message = "Vehicle is already in booking/offered status";
            statusCode = 405;
        }
        catch (ResourceNotFoundException e) {
            message = "Vehicle is not registered";
            statusCode = 404;
        }
        return ResponseEntity.status(statusCode).body(new ResponseMessage(message));

    }


    @PostMapping("/select-ride")
    public ResponseEntity<SelectResponse> selectRide(@Valid @RequestBody RequestRide requestRide) {
        String message;
        SelectResponse selectResponse;
        List<SelectedVehicleInfo> selectedVehicleInfoList = null;
        int statusCode;
        try {
            selectedVehicleInfoList = rideService.selectRide(requestRide);
            if (selectedVehicleInfoList == null || selectedVehicleInfoList.isEmpty()) {
                message = "OOPS! No vehicle avaible for the locations";
                statusCode = 200;
            }
            else {
                message = "Bingo :)First Vehicle has been booked for your request";
                statusCode = 200;
            }
        }
        catch (BadRequest badRequest) {
            message = "Invalid request necessary field not present";
            statusCode = 400;
        }
        catch (AlreadyInActiveBookingException e) {
            message = e.getMessage();
            statusCode = 400;
        }
        selectResponse = new SelectResponse(message, selectedVehicleInfoList);
        return ResponseEntity.status(statusCode).body(selectResponse);
    }

    @DeleteMapping("/end-ride/{userName}")
    public ResponseEntity<ResponseMessage> endRide(@PathVariable String userName) {
        String message;
        int statusCode;
        try {
            rideService.endRide(userName);
            message = "have ended the ride";
            statusCode = 202;
        }
        catch (ResourceNotFoundException e) {
            message = "No booking assigned to user";
            statusCode = 405;
        }
        return ResponseEntity.status(statusCode).body(new ResponseMessage(message));
    }

    @GetMapping("/ride-stats")
    public ResponseEntity<Map<String, UserStats>> getRideStats() {
        return ResponseEntity.ok(statService.getUserStatsMap());
    }
}
