package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.AuthenticationAuditLog;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AuthenticationEventType;
import com.shopsphere.auth.Repository.AuthenticationAuditLogRepository;
import com.shopsphere.auth.Service.AuthenticationAuditService;
import com.shopsphere.auth.Service.RequestContextService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticateAuditServiceImpl implements AuthenticationAuditService {

    @Autowired
    private AuthenticationAuditLogRepository authenticationAuditLogRepository;

    @Autowired
    private RequestContextService requestContextService;

    @Override
    public void log(User user, AuthenticationEventType eventType) {

        AuthenticationAuditLog auditLog = AuthenticationAuditLog.builder()
                .user(user)
                .eventType(eventType)
                .UserAgent(requestContextService.getUserAgent())
                .ipAddress(requestContextService.getClientIp())
                .build();

        authenticationAuditLogRepository.save(auditLog);

    }
}
