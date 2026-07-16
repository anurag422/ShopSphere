package com.shopsphere.auth.dto.response;

import com.shopsphere.auth.Enum.AuthenticationEventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationAuditResponse {

    private AuthenticationEventType eventType;

    private String ipAddress;

    private String UserAgent;

    private LocalDateTime createdAt;

}
