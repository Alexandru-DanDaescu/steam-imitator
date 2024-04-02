package com.steam.steamimitator.exceptions.client;

public class ClientUpdateException extends RuntimeException{

    public ClientUpdateException(String message, Throwable cause){
        super(message, cause);
    }
}
