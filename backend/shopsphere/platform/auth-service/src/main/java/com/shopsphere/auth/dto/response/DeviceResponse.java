package com.shopsphere.auth.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponse {

    private long id;

    private String deviceName;

    private String idAddress;

    private String userAgent;

    private LocalDateTime expiryDate;

    private LocalDateTime lastUsedAt;

    private boolean currentDevice;

}
