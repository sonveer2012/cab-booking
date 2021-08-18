package com.sonveer.cab.sharing.services;


import com.sonveer.cab.sharing.entity.request.OfferedRide;
import com.sonveer.cab.sharing.entity.request.RequestRide;
import com.sonveer.cab.sharing.entity.request.RideEnum;
import com.sonveer.cab.sharing.entity.response.SelectedVehicleInfo;
import com.sonveer.cab.sharing.errors.BadRequest;
import com.sonveer.cab.sharing.errors.AlreadyInActiveBookingException;
import com.sonveer.cab.sharing.errors.ResourceNotFoundException;

import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class RideService {

    @Autowired
    StatService     statService;
    @Autowired
    ResourceService resourceService;

    ConcurrentHashMap<String, Set<String>> offeredLocationVehicleNumberMap; // locaion-set<vehivleNUmber> info
    ConcurrentHashMap<String, String>      userVehicleBookingMap; // user-vehicle active booking info
    ConcurrentHashMap<String, String>      vehicleUserBookingMap; // vehicle-user booking mapping
    ConcurrentHashMap<String, OfferedRide> vehicleOfferedRideMap; // vehicle - ride-offered status
    ConcurrentHashMap<String, String>      vehicleCurrentLocation; // vehicle current location

    public RideService() {
        offeredLocationVehicleNumberMap = new ConcurrentHashMap<>();
        userVehicleBookingMap = new ConcurrentHashMap<>();
        vehicleOfferedRideMap = new ConcurrentHashMap<>();
        vehicleCurrentLocation = new ConcurrentHashMap<>();
        vehicleUserBookingMap = new ConcurrentHashMap<>();
    }

    public void offerRide(OfferedRide offeredRide) throws AlreadyInActiveBookingException, ResourceNotFoundException {
        statService.addUser(offeredRide.getUserName());
        if (userVehicleBookingMap.containsValue(offeredRide.getVehicleNumber()) || vehicleOfferedRideMap
                .containsKey(offeredRide.getVehicleNumber())) {
            throw new AlreadyInActiveBookingException();
        }
        else if (resourceService.getRequestedVehicleFromResource(offeredRide.getVehicleNumber()) == null) {
            throw new ResourceNotFoundException();
        }
        else {
            String locationKey = generateKey(offeredRide.getOrigin(), offeredRide.getDestination());
            Set<String> vehiclesAtLocation = new HashSet<>();
            if (offeredLocationVehicleNumberMap.containsKey(locationKey)) {
                vehiclesAtLocation = offeredLocationVehicleNumberMap.get(locationKey);
            }
            vehiclesAtLocation.add(offeredRide.getVehicleNumber());
            offeredLocationVehicleNumberMap.put(locationKey, vehiclesAtLocation);
            // update offer status
            vehicleOfferedRideMap.put(offeredRide.getVehicleNumber(), offeredRide);
            // update vehicleCurrentLocation
            vehicleCurrentLocation.put(offeredRide.getVehicleNumber(), locationKey);
            statService.updateRideStats(offeredRide.getUserName(), RideEnum.RIDE_OFFERED);
        }
    }

    public List<SelectedVehicleInfo> selectRide(RequestRide requestRide) throws BadRequest, AlreadyInActiveBookingException {
        statService.addUser(requestRide.getUserName());
        String key = generateKey(requestRide.getOrigin(), requestRide.getDestination());
        RideEnum rideEnum = requestRide.getPreferredCat();
        if (rideEnum == RideEnum.PREF_VEHICLE && requestRide.getCompany() == null) {
            throw new BadRequest();
        }

        if (userVehicleBookingMap.containsKey(requestRide.getUserName())) {
            throw new AlreadyInActiveBookingException();
        }

        Set<String> vehiclesSetForLocation = offeredLocationVehicleNumberMap.get(key);
        if(vehiclesSetForLocation == null){
            return null;
        }


        List<SelectedVehicleInfo> selectedVehicleInfoList = new ArrayList<>();
        for (String vehicleNumber : vehiclesSetForLocation) {
            OfferedRide offeredRide = vehicleOfferedRideMap.get(vehicleNumber);
            String vehicleCompany = resourceService.getRequestedVehicleFromResource(vehicleNumber).getCabCompnany();
            SelectedVehicleInfo selectedVehicleInfo = new SelectedVehicleInfo(offeredRide.getVehicleNumber(),
                                                                              vehicleCompany,
                                                                              offeredRide.getVehicleNumber(),
                                                                              offeredRide.getSeatCount());
            selectedVehicleInfo.setVehicleCompany(resourceService.getRequestedVehicleFromResource(vehicleNumber)
                                                                 .getCabCompnany());
            selectedVehicleInfoList.add(selectedVehicleInfo);
        }
        // Filter according to seat
        List<SelectedVehicleInfo> filteredSelectedVehicleInfoList = selectedVehicleInfoList.stream().filter(
                selectedVehicleInfo -> selectedVehicleInfo.getNumberOfSeats() >= requestRide.getSeatCount()).collect(
                Collectors.toList());
        // Remove active booking cabs
        filteredSelectedVehicleInfoList = filteredSelectedVehicleInfoList.stream()
                                                                         .filter(selectedVehicleInfo -> !vehicleUserBookingMap
                                                                                 .containsKey(selectedVehicleInfo.getVehicleNumber()))
                                                                         .collect(Collectors.toList());
        if (requestRide.getPreferredCat() == RideEnum.PREF_VEHICLE) {
            filteredSelectedVehicleInfoList = filteredSelectedVehicleInfoList.stream()
                                                                             .filter(selectedVehicleInfo -> selectedVehicleInfo
                                                                                     .getVehicleCompany()
                                                                                     .equals(requestRide.getCompany()))
                                                                             .collect(
                                                                                     Collectors.toList());
            filteredSelectedVehicleInfoList.sort(new Comparator<SelectedVehicleInfo>() {
                @Override
                public int compare(SelectedVehicleInfo o1, SelectedVehicleInfo o2) {
                    return 0;
                }
            });
        }
        filteredSelectedVehicleInfoList.sort(new Comparator<SelectedVehicleInfo>() {
            @Override
            public int compare(SelectedVehicleInfo o1, SelectedVehicleInfo o2) {
                return o1.getNumberOfSeats() - o2.getNumberOfSeats();
            }
        });

        if (filteredSelectedVehicleInfoList.size() > 0) {
            // mark active booking
            userVehicleBookingMap.put(requestRide.getUserName(),
                                      filteredSelectedVehicleInfoList.get(0).getVehicleNumber());
            statService.updateRideStats(requestRide.getUserName(), RideEnum.RIDE_SELECTED);
            vehicleUserBookingMap.put(filteredSelectedVehicleInfoList.get(0).getVehicleNumber(),
                                      requestRide.getUserName());
        }
        return filteredSelectedVehicleInfoList;
    }

    public void endRide(String userId) throws ResourceNotFoundException {
        if (!userVehicleBookingMap.containsKey(userId)) {
            throw new ResourceNotFoundException();
        }
        else {
            String vehicleNumber = userVehicleBookingMap.get(userId);
            String currentLocation = vehicleCurrentLocation.get(vehicleNumber);
            if (!StringUtil.isEmpty(currentLocation) && offeredLocationVehicleNumberMap.containsKey(
                    currentLocation)) {
                // Remove vehicle from current-location
                Set<String> vehicles = offeredLocationVehicleNumberMap.get(currentLocation);
                vehicles.remove(vehicleNumber);
                offeredLocationVehicleNumberMap.put(currentLocation, vehicles);
                /// remove offered ride
                vehicleOfferedRideMap.remove(vehicleNumber);
                // remove from userVehicleBookingConcurrentMap
                userVehicleBookingMap.remove(userId);
                // remove currentLocation
                vehicleCurrentLocation.remove(vehicleNumber);
                // remove reverse mapping info
                vehicleUserBookingMap.remove(vehicleNumber);
            }
        }
    }

    private String generateKey(String origin, String destination) {
        return origin + "::" + destination;
    }


}
