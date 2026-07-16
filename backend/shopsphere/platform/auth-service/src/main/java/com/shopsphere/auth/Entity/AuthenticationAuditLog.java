package com.shopsphere.auth.Entity;

import com.shopsphere.auth.Enum.AuthenticationEventType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authenticate_audit_log")
public class AuthenticationAuditLog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthenticationEventType eventType;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false,length = 1000)
    private String UserAgent;

}
