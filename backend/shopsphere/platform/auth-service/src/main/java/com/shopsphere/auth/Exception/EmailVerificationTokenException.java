package com.shopsphere.auth.Exception;

public class EmailVerificationTokenException extends RuntimeException{

    public EmailVerificationTokenException(String email){
        super(email);
    }

}
