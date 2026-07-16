package com.shopsphere.auth.Exception;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message){
        super(message);
    }

}
