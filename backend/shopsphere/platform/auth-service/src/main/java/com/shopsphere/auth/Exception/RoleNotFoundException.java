package com.shopsphere.auth.Exception;

public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(String role){
        super("Role not Found : "+role);
    }

}
