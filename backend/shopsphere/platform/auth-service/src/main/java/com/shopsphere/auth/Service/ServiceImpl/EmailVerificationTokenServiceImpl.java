package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.EmailVerificationToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Exception.EmailVerificationTokenException;
import com.shopsphere.auth.Repository.EmailVerificationTokenRepository;
import com.shopsphere.auth.Service.EmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    public EmailVerificationToken createVerificationToken(User user) {

        emailVerificationTokenRepository.deleteByUser(user);

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();

        emailVerificationToken.setUser(user);
        emailVerificationToken.setToken(UUID.randomUUID().toString());
        emailVerificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        EmailVerificationToken saved = emailVerificationTokenRepository.save(emailVerificationToken);

        return saved;

    }

    @Override
    public EmailVerificationToken verifyToken(String token) {

        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(() -> new EmailVerificationTokenException("Token is not found"));

        if (emailVerificationToken.isUsed()){
            throw new EmailVerificationTokenException("Email is not valid");
        }
        if (emailVerificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new EmailVerificationTokenException("Token is expired");
        }

        return emailVerificationToken;

    }

    @Override
    public void markTokenAsUsed(EmailVerificationToken token) {

        token.setUsed(true);

        emailVerificationTokenRepository.save(token);

    }
}
