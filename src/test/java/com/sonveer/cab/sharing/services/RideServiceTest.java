package com.sonveer.cab.sharing.services;

import com.sonveer.cab.sharing.entity.request.OfferedRide;
import com.sonveer.cab.sharing.entity.request.RequestRide;
import com.sonveer.cab.sharing.entity.request.RequestVehicleObject;
import com.sonveer.cab.sharing.entity.request.RideEnum;
import com.sonveer.cab.sharing.entity.request.UserStats;
import com.sonveer.cab.sharing.entity.response.SelectedVehicleInfo;
import com.sonveer.cab.sharing.errors.AlreadyInActiveBookingException;
import com.sonveer.cab.sharing.errors.BadRequest;
import com.sonveer.cab.sharing.errors.ResourceAlreadyExistException;
import com.sonveer.cab.sharing.errors.ResourceNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@WebAppConfiguration
@SpringBootTest(classes = {ResourceService.class, StatService.class, RideService.class})

@RunWith(SpringJUnit4ClassRunner.class)
public class RideServiceTest {

    @Autowired
    StatService     statService;
    @Autowired
    ResourceService resourceService;
    @Autowired
    RideService     rideService;

    @Test
    public void testOfferedRideWithRegistredCabAndValidBooking() throws ResourceAlreadyExistException, AlreadyInActiveBookingException, ResourceNotFoundException, BadRequest {
        List<String> vehicleNumber = new ArrayList<>();
        vehicleNumber.add("KA01-1111");
        vehicleNumber.add("KA01-1112");
        vehicleNumber.add("KA01-1113");
        RequestVehicleObject requestVehicleObject = new RequestVehicleObject("TestUser", "AUDI", vehicleNumber);
        // offer two rides
        resourceService.addVehicle(requestVehicleObject);
        OfferedRide offeredRide = new OfferedRide("goa", "delhi", "testUser", 2, "KA01-1111");
        rideService.offerRide(offeredRide);
        OfferedRide offeredRide1 = new OfferedRide("goa", "delhi", "testUser", 5, "KA01-1112");
        rideService.offerRide(offeredRide1);
        OfferedRide offeredRide2 = new OfferedRide("goa", "delhi", "testUser", 5, "KA01-1113");
        rideService.offerRide(offeredRide2);
        RequestRide requestRide = new RequestRide("goa", "delhi", "sonveer", 2, RideEnum.MOST_VACANT);

        List<SelectedVehicleInfo> selectedVehicleInfoList = rideService.selectRide(requestRide);

        Assert.assertEquals(selectedVehicleInfoList.size(), 3);
        SelectedVehicleInfo selectedVehicleInfo = selectedVehicleInfoList.get(0);

        Assert.assertEquals(selectedVehicleInfo.getNumberOfSeats().intValue(), 5);
        Assert.assertEquals(selectedVehicleInfo.getVehicleNumber(), "KA01-1112");

        // DELET THE BOOKING
        RequestRide requestRide1 = new RequestRide("goa", "delhi", "sonveer1", 2, RideEnum.MOST_VACANT);
        selectedVehicleInfoList = rideService.selectRide(requestRide1);

        Assert.assertEquals(selectedVehicleInfoList.size(), 2);
        selectedVehicleInfo = selectedVehicleInfoList.get(0);

        Assert.assertEquals(selectedVehicleInfo.getNumberOfSeats().intValue(), 5);
        Assert.assertEquals(selectedVehicleInfo.getVehicleNumber(), "KA01-1113");

        // End the trip
        rideService.endRide("sonveer");
        rideService.endRide("sonveer1");

        // Offer a new ride again
        rideService.offerRide(offeredRide1);
        rideService.offerRide(offeredRide2);
        requestRide1 = new RequestRide("goa", "delhi", "sonveer", 3, RideEnum.PREF_VEHICLE, "AUDI");
        selectedVehicleInfoList = rideService.selectRide(requestRide1);
        Assert.assertEquals(selectedVehicleInfoList.size(), 2);
        selectedVehicleInfo = selectedVehicleInfoList.get(0);

        Assert.assertEquals(selectedVehicleInfo.getNumberOfSeats().intValue(), 5);
        Assert.assertEquals(selectedVehicleInfo.getVehicleNumber(), "KA01-1112");

        // check with different company
        requestRide1 = new RequestRide("goa", "delhi", "sonveer1", 3, RideEnum.PREF_VEHICLE, "BMW");
        selectedVehicleInfoList = rideService.selectRide(requestRide1);
        Assert.assertEquals(selectedVehicleInfoList.size(), 0);

        // check with different company
        requestRide1 = new RequestRide("delhi", "goa", "sonveer1", 3, RideEnum.PREF_VEHICLE, "AUDI");
        selectedVehicleInfoList = rideService.selectRide(requestRide1);
        Assert.assertNull(selectedVehicleInfoList);

        // Valid the status Again
        ConcurrentHashMap<String, UserStats> statServiceMap = statService.getUserStatsMap();
        UserStats userStatsSonveer = statServiceMap.get("sonveer");
        Assert.assertEquals(0, userStatsSonveer.getOfferedRide().intValue());
        Assert.assertEquals(2, userStatsSonveer.getRideTaken().intValue());

        UserStats userStatsSonveer1 = statServiceMap.get("sonveer1");
        Assert.assertEquals(0, userStatsSonveer1.getOfferedRide().intValue());
        Assert.assertEquals(1, userStatsSonveer1.getRideTaken().intValue());

        UserStats userStatsTestUser = statServiceMap.get("TestUser");
        Assert.assertEquals(0, userStatsTestUser.getOfferedRide().intValue());
        Assert.assertEquals(0, userStatsTestUser.getRideTaken().intValue());

        UserStats userStatstestUser = statServiceMap.get("testUser");
        Assert.assertEquals(5, userStatstestUser.getOfferedRide().intValue());
        Assert.assertEquals(0, userStatstestUser.getRideTaken().intValue());


    }

}
