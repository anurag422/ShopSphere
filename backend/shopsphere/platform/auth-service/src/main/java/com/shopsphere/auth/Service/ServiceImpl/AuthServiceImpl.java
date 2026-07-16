package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.*;
import com.shopsphere.auth.Enum.AccountStatus;
import com.shopsphere.auth.Enum.AuthenticationEventType;
import com.shopsphere.auth.Enum.RoleType;
import com.shopsphere.auth.Exception.*;
import com.shopsphere.auth.Repository.AuthenticationAuditLogRepository;
import com.shopsphere.auth.Repository.RoleRepository;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.*;
import com.shopsphere.auth.dto.request.*;
import com.shopsphere.auth.dto.response.AuthenticationAuditResponse;
import com.shopsphere.auth.dto.response.AuthenticationResponse;
import com.shopsphere.auth.dto.response.RefreshTokenResponse;
import com.shopsphere.auth.dto.response.RegisterResponse;
import com.shopsphere.auth.security.JwtService;
import com.shopsphere.auth.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private AccountLockService accountLockService;

    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;

    @Autowired
    private AuthenticationAuditService authenticationAuditService;

    @Autowired
    private AuthenticationAuditLogRepository authenticationAuditLogRepository;

    @Autowired
    private SecurityUtil securityUtil;


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

        authenticationAuditService.log(user,AuthenticationEventType.REGISTER);

        EmailVerificationToken verificationToken = emailVerificationTokenService.createVerificationToken(user);

        emailService.sendEmailVerification(user.getEmail(), verificationToken.getToken());

        return RegisterResponse.builder().email(user.getEmail()).userId(user.getId()).message("Registered Successfully please verify your Email").build();

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (!user.isAccountNonLocked()){
            if (!accountLockService.unlockIfExpired(user)){
                throw new AccountLockedException("Account is Locked Try again later");
            }
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (BadCredentialsException e){
            accountLockService.loginFailed(user);
            throw e;
        }
        if (!user.isEmailVerified()){
            authenticationAuditService.log(user,AuthenticationEventType.LOGIN_FAILED);
            throw new EmailVerificationTokenException("please verify the email before logging");
        }

        accountLockService.loginSucceeded(user);

        authenticationAuditService.log(user, AuthenticationEventType.LOGIN_SUCCESS);

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
    public RefreshTokenResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(token);

        User user = refreshToken.getUser();

        String generateToken = jwtService.generateToken(user);

        return RefreshTokenResponse.builder().accessToken(generateToken).expiresIn(accessTokenExpiration).tokenType("Bearer").build();

    }

    @Override
    public void logOut(String token) {

        User user = securityUtil.getCurrentUser();

        refreshTokenService.revokeRefreshToken(token);

        authenticationAuditService.log(user,AuthenticationEventType.LOGOUT);

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

        PasswordResetToken passwordResetToken = passwordResetTokenService.verifyPasswordResetToken(request.getPassword());

        User user = passwordResetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        authenticationAuditService.log(user,AuthenticationEventType.PASSWORD_RESET);

        passwordResetTokenService.markTokenAsUsed(passwordResetToken);

    }

    @Override
    public void emailVerify(String token) {

        System.out.println( "verify method reached "+token);

        EmailVerificationToken verificationToken = emailVerificationTokenService.verifyToken(token);

        System.out.println("after search "+verificationToken.getToken());

        User user = verificationToken.getUser();

        user.setEmailVerified(true);

        userRepository.save(user);

        emailVerificationTokenService.markTokenAsUsed(verificationToken);

        authenticationAuditService.log(user,AuthenticationEventType.EMAIL_VERIFIED);

    }

    @Override
    public void resendVerificationEmail(ResendEmailVerificationRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResendEmailVerificationException("User is not found"));

        if (user.isEmailVerified()){
            throw new ResendEmailVerificationException("Email Already Verify");
        }

        EmailVerificationToken verificationToken = emailVerificationTokenService.createVerificationToken(user);

        emailService.sendEmailVerification(user.getEmail(), verificationToken.getToken());

        authenticationAuditService.log(user,AuthenticationEventType.EMAIL_VERIFIED_RESENT);

    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("not found"));

        if (!passwordEncoder.matches(user.getPassword(), request.getCurrentPassword())){
            throw new InvalidPasswordException("Invalid Password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        authenticationAuditService.log(user,AuthenticationEventType.PASSWORD_CHANGE);

        refreshTokenService.revokeAllUserTokens(user);

    }

    @Override
    public List<AuthenticationAuditResponse> getCurrentUserLog() {


        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = principal.getUsername();

        User user = userRepository.findByEmail(username).orElse(null);

        return authenticationAuditLogRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(this::mapToResponse).toList();

    }

    private AuthenticationAuditResponse mapToResponse(AuthenticationAuditLog log){
        return AuthenticationAuditResponse.builder()
                .eventType(log.getEventType())
                .ipAddress(log.getIpAddress())
                .UserAgent(log.getUserAgent())
                .createdAt(log.getCreatedAt())
                .build();
    }


}
