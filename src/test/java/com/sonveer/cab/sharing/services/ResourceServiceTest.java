package com.sonveer.cab.sharing.services;

import com.sonveer.cab.sharing.entity.request.Gender;
import com.sonveer.cab.sharing.entity.request.RequestVehicleObject;
import com.sonveer.cab.sharing.entity.request.User;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@SpringBootTest(classes = {ResourceService.class, StatService.class})

@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceServiceTest {

    @Autowired
    StatService     statService;
    @Autowired
    ResourceService resourceService;

    @Test
    public void addAnUserTest() {
        User excpectedUser = new User("Sonveer", Gender.MALE, 25);
        try {
            resourceService.addUser(excpectedUser);
            User actualUser = resourceService.getRequestedUserFromResource(excpectedUser.getName());
            Assert.assertEquals(actualUser.getAge(), excpectedUser.getAge());
            Assert.assertEquals(actualUser.getGender(), excpectedUser.getGender());
            Assert.assertEquals(actualUser.getName(), excpectedUser.getName());
        }
        catch (Exception ignored) {

        }
        try {
            resourceService.addUser(excpectedUser);
        }
        catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "User with given name already existing");
        }
    }

    @Test
    public void addVehicleTest() {
        List<String> vehicleNumber = new ArrayList<>();
        vehicleNumber.add("123");
        vehicleNumber.add("456");
        vehicleNumber.add("789");
        RequestVehicleObject expectedVehicleObject = new RequestVehicleObject("TestUser", "AUDI", vehicleNumber);
        try {
            resourceService.addVehicle(expectedVehicleObject);
            RequestVehicleObject actualVehicleObject = resourceService.getRequestedVehicleFromResource("456");
            Assert.assertEquals(expectedVehicleObject.getCabCompnany(), actualVehicleObject.getCabCompnany());
            Assert.assertEquals(expectedVehicleObject.getUserName(), actualVehicleObject.getUserName() );
            RequestVehicleObject actualVehicleObject1  = resourceService.getRequestedVehicleFromResource("145");
            Assert.assertNull(actualVehicleObject1);
        }
        catch (Exception ignored) {
        }

        try {
            resourceService.addVehicle(expectedVehicleObject);
        }
        catch (Exception e) {
            Assert.assertEquals("One or more vehicle already exsited in system , No vehicle has been added", e.getMessage());
        }

    }

}
