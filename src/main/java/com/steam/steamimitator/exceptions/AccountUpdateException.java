package com.steam.steamimitator.exceptions;

public class AccountUpdateException extends RuntimeException{

    public AccountUpdateException(String message, Throwable cause){
        super(message, cause);
    }
}
