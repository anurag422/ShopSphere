package com.shopsphere.auth.Service;

import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.AuthenticationResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import com.shopsphere.auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logOut(LogoutRequest request);

    void forgetPassword(ForgetPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
