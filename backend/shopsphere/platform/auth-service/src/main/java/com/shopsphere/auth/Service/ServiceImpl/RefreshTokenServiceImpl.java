package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Exception.RefreshTokenException;
import com.shopsphere.auth.Repository.RefreshTokenRepository;
import com.shopsphere.auth.Service.RefreshTokenService;
import com.shopsphere.auth.dto.request.RefreshTokenRequest;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(generateRefreshToken());
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("Refresh Token is not found"));

        if (refreshToken.isRevoked()){
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token is expired");
        }

        return refreshToken;
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void revokeRefreshToken(String token) {

        System.out.println("Recieved refresh token : "+token);

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("Refresh token is not found"));

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

    }


}
