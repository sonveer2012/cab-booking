package com.sonveer.cab.sharing.services;

import com.sonveer.cab.sharing.entity.request.RideEnum;
import com.sonveer.cab.sharing.entity.request.UserStats;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class StatService {
    private final ConcurrentHashMap<String, UserStats> userStatsMap;
    public StatService(){
        this.userStatsMap = new ConcurrentHashMap<>();
    }

    public void addUser(String userName) {
        if(!userStatsMap.containsKey(userName)){
            UserStats userStats = new UserStats();
            userStatsMap.put(userName, userStats);
        }
    }

    public void updateRideStats(String userId, RideEnum rideEnum){
        UserStats userStats = userStatsMap.get(userId);
        if(userStats == null){
            userStats = new UserStats();
        }
        if(rideEnum == RideEnum.RIDE_OFFERED){
            userStats.increaseOfferRide();
        } else{
            userStats.increaseTakenRide();
        }
        userStatsMap.put(userId,userStats);
    }

    public ConcurrentHashMap<String, UserStats> getUserStatsMap() {
        return userStatsMap;
    }
}
