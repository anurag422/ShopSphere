package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.PasswordResetToken;
import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.Role;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AccountStatus;
import com.shopsphere.auth.Enum.RoleType;
import com.shopsphere.auth.Exception.EmailAlreadyExistsException;
import com.shopsphere.auth.Exception.PasswordResetTokenException;
import com.shopsphere.auth.Exception.RoleNotFoundException;
import com.shopsphere.auth.Exception.UserNotFoundException;
import com.shopsphere.auth.Repository.PasswordResetTokenRepo;
import com.shopsphere.auth.Repository.RoleRepository;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.AuthService;
import com.shopsphere.auth.Service.EmailService;
import com.shopsphere.auth.Service.PasswordResetTokenService;
import com.shopsphere.auth.Service.RefreshTokenService;
import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.AuthenticationResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import com.shopsphere.auth.dto.response.RegisterResponse;
import com.shopsphere.auth.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;


    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already Exist");
        }

        Role role =  roleRepository.findByName(RoleType.ROLE_CUSTOMER).orElseThrow(()->new RoleNotFoundException("ROLE_CUSTOMER"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(false)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        return RegisterResponse.builder().email(user.getEmail()).userId(user.getId()).message("Registered Successfully").build();

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String generateToken = jwtService.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        System.out.println("Generated token : "+refreshToken);

        return AuthenticationResponse.builder()
                .AccessToken(generateToken)
                .RefreshToken(refreshToken.getToken())
                .expiresIn(accessTokenExpiration)
                .tokenType("Bearer")
                .build();

    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        User user = refreshToken.getUser();

        String generateToken = jwtService.generateToken(user);

        return RefreshTokenResponse.builder().accessToken(generateToken).expiresIn(accessTokenExpiration).tokenType("Bearer").build();

    }

    @Override
    public void logOut(LogoutRequest request) {

        refreshTokenService.revokeRefreshToken(request.getRefreshToken());

    }

    @Override
    @Transactional
    public void forgetPassword(ForgetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);

        emailService.sendPasswordResetEmail(user.getEmail(),passwordResetToken.getToken());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken byToken = passwordResetTokenRepo.findByToken(request.getToken()).orElseThrow(()->new PasswordResetTokenException("Token is not found"));

        User user = byToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        passwordResetTokenService.markTokenAsUsed(byToken);

    }


}
