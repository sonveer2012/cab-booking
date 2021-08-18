package com.sonveer.cab.sharing.errors;

public class AlreadyInActiveBookingException extends Exception{
    public AlreadyInActiveBookingException(){
        super("Resource already booked/offered state can't offerRide");
    }
}
