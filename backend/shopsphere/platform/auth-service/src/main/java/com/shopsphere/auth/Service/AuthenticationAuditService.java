package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AuthenticationEventType;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationAuditService {

    void log(User user, AuthenticationEventType eventType);
}
