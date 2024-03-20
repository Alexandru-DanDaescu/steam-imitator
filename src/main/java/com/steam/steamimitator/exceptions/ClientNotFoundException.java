package com.steam.steamimitator.exceptions;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message){
        super(message);
    }
}
