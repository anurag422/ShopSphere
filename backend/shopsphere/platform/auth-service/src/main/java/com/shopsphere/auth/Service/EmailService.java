package com.shopsphere.auth.Service;

public interface EmailService {

    void sendPasswordResetEmail(String email,String token);

    void sendEmailVerification(String email,String token);

}
