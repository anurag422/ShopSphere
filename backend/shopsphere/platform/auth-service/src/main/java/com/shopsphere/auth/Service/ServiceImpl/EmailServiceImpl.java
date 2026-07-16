package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendPasswordResetEmail(String email, String token) {

        SimpleMailMessage mailMessage =  new SimpleMailMessage();

        mailMessage.setTo(email);

        mailMessage.setSubject("ShopSphere Password Reset");

        mailMessage.setText("Click the link below to reset you password:\n\n"+"http://localhost:3000/reset-password?token="+token);

        javaMailSender.send(mailMessage);

    }

    @Override
    public void sendEmailVerification(String email, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Verify your ShopSphere Account");

        message.setText(
                """
                Welcome to ShopSphere!
    
                Click the link below to verify your email.
    
                http://localhost:3000/api/v1/auth/verify-email?token=%s
                """.formatted(token)
        );

        javaMailSender.send(message);

    }


}
