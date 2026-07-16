package com.shopsphere.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendEmailVerificationRequest {

    @Email
    @NotBlank
    private String email;

}
