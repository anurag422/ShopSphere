package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.dto.request.RefreshTokenRequest;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;


public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    String generateRefreshToken();

    void revokeRefreshToken(String token);

}
