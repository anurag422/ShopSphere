package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Exception.RefreshTokenException;
import com.shopsphere.auth.Repository.RefreshTokenRepository;
import com.shopsphere.auth.Service.DeviceInfoService;
import com.shopsphere.auth.Service.RefreshTokenService;
import com.shopsphere.auth.Service.RequestContextService;
import com.shopsphere.auth.dto.response.DeviceResponse;
import com.shopsphere.auth.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RequestContextService requestContextService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken =

        RefreshToken.builder()
                .token(generateRefreshToken())
                .user(user)
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration))
                .ipAddress(requestContextService.getClientIp())
                .userAgent(requestContextService.getUserAgent())
                .deviceName(deviceInfoService.getDeviceName())
                .lastUsedAt(LocalDateTime.now())
                .build();

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

    @Override
    public void revokeAllUserTokens(User user) {

        List<RefreshToken> byUser = refreshTokenRepository.findByUser(user);

        byUser.forEach(token->token.setRevoked(true));

        refreshTokenRepository.saveAll(byUser);

    }

    @Override
    public List<DeviceResponse> getMyDevice() {

        User currentUser = securityUtil.getCurrentUser();

        return refreshTokenRepository.findByUserAndRevokedFalse(currentUser)
                .stream().map(this::mapToResponse).toList();

    }

    @Override
    public void logOutDevice(Long id) {

        User currentUser = securityUtil.getCurrentUser();

        RefreshToken refreshToken = refreshTokenRepository.findByIdAndUser(id, currentUser).orElseThrow();

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void logOutAllOtherDevice() {

        User currentUser = securityUtil.getCurrentUser();

        List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedFalse(currentUser);



    }

    @Override
    public String getRefreshToken(HttpServletRequest request) {

        if (request.getCookies() == null){
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken")).map(Cookie::getValue).findFirst().orElseThrow();

    }

    private DeviceResponse mapToResponse(RefreshToken token){
        return DeviceResponse.builder()
                .id(token.getId())
                .deviceName(token.getDeviceName())
                .idAddress(token.getIpAddress())
                .userAgent(token.getUserAgent())
                .lastUsedAt(token.getLastUsedAt())
                .expiryDate(token.getExpiryDate())
                .build();
    }


}
