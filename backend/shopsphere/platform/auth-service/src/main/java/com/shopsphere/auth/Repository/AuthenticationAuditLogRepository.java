package com.shopsphere.auth.Repository;

import com.shopsphere.auth.Entity.AuthenticationAuditLog;
import com.shopsphere.auth.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticationAuditLogRepository extends JpaRepository<AuthenticationAuditLog,Long> {

    List<AuthenticationAuditLog> findByUserOrderByCreatedAtDesc(User user);

    Page<AuthenticationAuditLog> findByUser(User user, Pageable pageable);

}
