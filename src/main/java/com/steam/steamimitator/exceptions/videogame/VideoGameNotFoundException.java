package com.steam.steamimitator.exceptions.videogame;

public class VideoGameNotFoundException extends RuntimeException{

    public VideoGameNotFoundException(String message){
        super(message);
    }
}
