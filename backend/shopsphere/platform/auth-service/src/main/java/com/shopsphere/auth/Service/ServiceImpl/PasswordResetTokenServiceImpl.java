package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.PasswordResetToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Exception.PasswordResetTokenException;
import com.shopsphere.auth.Repository.PasswordResetTokenRepo;
import com.shopsphere.auth.Service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Value("${password-reset.expiration}")
    private long expiration;

    @Override
    public PasswordResetToken createPasswordResetToken(User user) {

        passwordResetTokenRepo.deleteByUser(user);

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusSeconds(expiration));

        return passwordResetTokenRepo.save(passwordResetToken);

    }

    @Override
    public PasswordResetToken verifyPasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token).orElseThrow(() -> new PasswordResetTokenException("Password Reset token is not found"));

        if (resetToken.isUsed()){
            throw  new PasswordResetTokenException("Reset Password token is Used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new PasswordResetTokenException("Reset token is Expired");
        }

        return resetToken;

    }

    @Override
    public void markTokenAsUsed(PasswordResetToken token) {
        token.setUsed(true);

        passwordResetTokenRepo.save(token);
    }
}
