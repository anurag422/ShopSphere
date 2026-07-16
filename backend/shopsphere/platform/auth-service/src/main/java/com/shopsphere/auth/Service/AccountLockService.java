package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.User;

public interface AccountLockService {

    void loginSucceeded(User user);

    void loginFailed(User user);

    boolean unlockIfExpired(User user);

}
