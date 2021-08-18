package com.sonveer.cab.sharing.services;

import com.sonveer.cab.sharing.entity.request.RideEnum;
import com.sonveer.cab.sharing.entity.request.UserStats;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.ConcurrentHashMap;

@WebAppConfiguration
@SpringBootTest(classes = {StatService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StatsServiceTest {
    @Autowired
    StatService statService;

    @Test
    public void userStatServiceTest() {
        statService.updateRideStats("123", RideEnum.RIDE_OFFERED);
        statService.updateRideStats("123", RideEnum.RIDE_OFFERED);
        statService.updateRideStats("123", RideEnum.RIDE_OFFERED);
        statService.updateRideStats("123", RideEnum.RIDE_OFFERED);
        statService.updateRideStats("123", RideEnum.RIDE_SELECTED);
        statService.updateRideStats("456", RideEnum.RIDE_SELECTED);
        statService.addUser("789");

        ConcurrentHashMap<String, UserStats> concurrentHashMap = statService.getUserStatsMap();
        UserStats userStats123 = concurrentHashMap.get("123");
        Assert.assertEquals(userStats123.getOfferedRide().intValue(), 4);
        Assert.assertEquals(userStats123.getRideTaken().intValue(), 1);

        UserStats userStats456 = concurrentHashMap.get("456");
        Assert.assertEquals(userStats456.getOfferedRide().intValue(), 0);
        Assert.assertEquals(userStats456.getRideTaken().intValue(), 1);

        UserStats userStats789 = concurrentHashMap.get("789");
        Assert.assertEquals(userStats789.getOfferedRide().intValue(), 0);
        Assert.assertEquals(userStats789.getRideTaken().intValue(), 0);

        UserStats userStats145 = concurrentHashMap.get("sonveer");
        Assert.assertNull(userStats145);
    }
}
