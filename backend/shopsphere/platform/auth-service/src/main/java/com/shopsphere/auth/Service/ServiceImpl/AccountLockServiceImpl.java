package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AuthenticationEventType;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.AccountLockService;
import com.shopsphere.auth.Service.AuthenticationAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountLockServiceImpl implements AccountLockService {

    @Value("${security.login.max-attempts}")
    private int maxAttempts;

    @Value("${security.login.lock-duration}")
    private long lockDuration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationAuditService authenticationAuditService;

    @Override
    public void loginSucceeded(User user) {

        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);
        user.setLockTime(null);

        userRepository.save(user);

    }

    @Override
    public void loginFailed(User user) {

        int attempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        if (attempts >= maxAttempts){

            user.setAccountNonLocked(false);

            user.setLockTime(LocalDateTime.now());

        }

        userRepository.save(user);
        authenticationAuditService.log(user, AuthenticationEventType.ACCOUNT_LOCKED);

    }

    @Override
    public boolean unlockIfExpired(User user) {

        if (user.isAccountNonLocked()) {
            return true;
        }

        if (user.getLockTime() == null) {
            throw new IllegalStateException(
                    "Locked account has no lock time.");
        }

        LocalDateTime unlockTime =
                user.getLockTime().plusSeconds(lockDuration);

        if (LocalDateTime.now().isAfter(unlockTime)) {

            user.setAccountNonLocked(true);
            user.setFailedLoginAttempts(0);
            user.setLockTime(null);

            userRepository.save(user);

            authenticationAuditService.log(
                    user,
                    AuthenticationEventType.ACCOUNT_UNLOCKED
            );

            return true;
        }

        return false;
    }
}
