package com.shopsphere.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank
    private String password;

}
