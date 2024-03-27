package com.steam.steamimitator.exceptions.account;

public class AccountUpdateException extends RuntimeException{

    public AccountUpdateException(String message, Throwable cause){
        super(message, cause);
    }
}
