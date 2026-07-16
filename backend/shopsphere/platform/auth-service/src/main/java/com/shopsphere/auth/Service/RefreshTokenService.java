package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.dto.request.RefreshTokenRequest;
import com.shopsphere.auth.dto.response.DeviceResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    String generateRefreshToken();

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(User user);

    List<DeviceResponse> getMyDevice();

    void logOutDevice(Long id);

    void logOutAllOtherDevice();

    String getRefreshToken(HttpServletRequest request);

}
