package com.steam.steamimitator.exceptions;

public class VideoGameNotFoundException extends RuntimeException{

    public VideoGameNotFoundException(String message){
        super(message);
    }
}
