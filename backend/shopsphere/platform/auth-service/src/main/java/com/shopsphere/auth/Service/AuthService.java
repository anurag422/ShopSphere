package com.shopsphere.auth.Service;

import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.AuthenticationAuditResponse;
import com.shopsphere.auth.dto.response.AuthenticationResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import com.shopsphere.auth.dto.response.RegisterResponse;

import java.util.List;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(String token);

    void logOut(String token);

    void forgetPassword(ForgetPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void emailVerify(String token);

    void resendVerificationEmail(ResendEmailVerificationRequest request);

    void changePassword(ChangePasswordRequest request);

    List<AuthenticationAuditResponse> getCurrentUserLog();

}
