package com.shopsphere.auth.controller;

import com.shopsphere.auth.Service.AuthService;
import com.shopsphere.auth.Service.RefreshTokenService;
import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.AuthenticationResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import com.shopsphere.auth.dto.response.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> create(@Valid @RequestBody RegisterRequest request){
        System.out.println(request);
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request){

        AuthenticationResponse logined = authService.login(request);

        return ResponseEntity.ok(logined);
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(@RequestBody LogoutRequest request){

        authService.logOut(request);

        return ResponseEntity.ok(" Logged out Successfully ");

    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request){

        authService.forgetPassword(request);

        return ResponseEntity.ok("Password Reset email is send to your email");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){

        authService.resetPassword(request);

        return ResponseEntity.ok("Your password Reset Successfully");
    }

}
