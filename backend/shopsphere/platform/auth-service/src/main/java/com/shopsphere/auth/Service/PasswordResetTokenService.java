package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.PasswordResetToken;
import com.shopsphere.auth.Entity.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken verifyPasswordResetToken(String token);

    void markTokenAsUsed(PasswordResetToken token);

}
