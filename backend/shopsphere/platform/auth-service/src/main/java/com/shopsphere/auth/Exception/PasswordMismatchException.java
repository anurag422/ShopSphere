package com.shopsphere.auth.Exception;

public class PasswordMismatchException extends RuntimeException{

    public PasswordMismatchException(){
        super("Password and confirmPassword is not match");
    }

}
