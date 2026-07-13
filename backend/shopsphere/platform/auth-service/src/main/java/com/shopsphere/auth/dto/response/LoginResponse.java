package com.shopsphere.auth.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private String type;

    private Long expiresIn;

}
