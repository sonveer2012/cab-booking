package com.sonveer.cab.sharing.errors;

public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(){
        super("Resource not found");
    }
}
