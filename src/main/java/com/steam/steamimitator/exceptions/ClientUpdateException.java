package com.steam.steamimitator.exceptions;

public class ClientUpdateException extends RuntimeException{

    public ClientUpdateException(String message, Throwable cause){
        super(message, cause);
    }
}
