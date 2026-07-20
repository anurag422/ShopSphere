package com.shopshpere.user.Exception;

public class ProfileAlreadyExistException extends RuntimeException {

    public ProfileAlreadyExistException(String message){
        super(message);
    }

}
