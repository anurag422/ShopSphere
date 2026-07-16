package com.shopsphere.auth.Service;


import com.shopsphere.auth.Entity.EmailVerificationToken;
import com.shopsphere.auth.Entity.User;

public interface EmailVerificationTokenService {

    EmailVerificationToken createVerificationToken(User user);

    EmailVerificationToken verifyToken(String token);

    void markTokenAsUsed(EmailVerificationToken token);

}
