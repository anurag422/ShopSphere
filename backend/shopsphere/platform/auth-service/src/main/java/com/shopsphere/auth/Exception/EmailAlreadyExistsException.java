package com.shopsphere.auth.Exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email){
        super("Email is already Exist : "+email);
    }

}
