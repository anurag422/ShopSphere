package com.shopsphere.auth.controller;

import com.shopsphere.auth.Service.AuthService;
import com.shopsphere.auth.Service.CookieService;
import com.shopsphere.auth.Service.RefreshTokenService;
import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private CookieService cookieService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RegisterRequest request){
        System.out.println(request);

        return ResponseEntity.ok(ApiResponse.<RegisterResponse>builder()
                .message("User Register successfully")
                        .success(true)
                .data(authService.register(request))
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){

        AuthenticationResponse logined = authService.login(request);

        cookieService.addRefreshTokenCookie(response, logined.getRefreshToken());

        logined.setRefreshToken(null);

        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .message("User Login Successfully")
                .data(logined)
                        .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication){

        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refresh(@Valid HttpServletRequest request){

        String refreshToken = refreshTokenService.getRefreshToken(request);

        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
                .message("Refresh token is Regenrated")
                .data(authService.refreshToken(refreshToken))
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());

    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logOut(HttpServletRequest request,HttpServletResponse response){

        String refreshToken = refreshTokenService.getRefreshToken(request);

        authService.logOut(refreshToken);

        cookieService.deleteRefreshTokenCookie(response);

        return ResponseEntity.ok(ApiResponse.builder().message("Logout successful")
                .success(true).data("Logout success")
                .timeStamp(LocalDateTime.now()).build());

    }

    @PostMapping("/forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request){

        authService.forgetPassword(request);

        return ResponseEntity.ok(ApiResponse.builder().message("Password Forget Email")
                .success(true).data("Forget Password Email is send to your Email")
                .timeStamp(LocalDateTime.now()).build());

    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request){

        authService.resetPassword(request);

        return ResponseEntity.ok(ApiResponse.builder().message("Reset Password")
                .success(true).data("Your Password has been reset Successfully")
                .timeStamp(LocalDateTime.now()).build());
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@Valid @RequestParam String token){
        System.out.println(token);
        authService.emailVerify(token);

        return ResponseEntity.ok(ApiResponse.builder().message("Email Verified")
                .success(true).data("Email is verified")
                .timeStamp(LocalDateTime.now()).build());
    }

    @PostMapping("/resend-email-verify")
    public ResponseEntity<ApiResponse> resendVerifyEmail(@Valid @RequestBody ResendEmailVerificationRequest request){
        authService.resendVerificationEmail(request);

        return ResponseEntity.ok(ApiResponse.builder().message("Resend Verification Email")
                .success(true).data("Verification email resend successfully")
                .timeStamp(LocalDateTime.now()).build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ChangePasswordRequest request){
        authService.changePassword(request);

        return ResponseEntity.ok(ApiResponse.builder().message("Password Change")
                .success(true).data("Password has been changed successfully")
                .timeStamp(LocalDateTime.now()).build());
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuthenticationAuditResponse>> getAuditLogs(){
        return ResponseEntity.ok(authService.getCurrentUserLog());
    }

    @GetMapping("/devices")
    public ResponseEntity<ApiResponse> getDevices(){

        return ResponseEntity.ok(ApiResponse.<List<DeviceResponse>>builder().message("Email Verified")
                .success(true).data(refreshTokenService.getMyDevice())
                .timeStamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> logOutDevice(@PathVariable Long id){
        refreshTokenService.logOutDevice(id);

        return ResponseEntity.noContent().build();
    }

}
