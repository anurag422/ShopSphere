package com.shopsphere.auth.Service;

public interface EmailService {

    void sendPasswordResetEmail(String email,String token);

}
