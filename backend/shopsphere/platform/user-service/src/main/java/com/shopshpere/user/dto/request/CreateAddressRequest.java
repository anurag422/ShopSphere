package com.shopshpere.user.dto.request;

import com.shopshpere.user.Enum.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    @NotBlank(message = "Name is Required")
    private String fullName;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number.")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 225)
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    private String city;

    @NotBlank(message = "Required Field")
    @Size(max = 50)
    private String state;

    @NotBlank(message = "Required Field")
    @Size(max = 50)
    private String country;

    @NotBlank(message = "Required Field")
    @Size(max = 100)
    private String postalCode;

    @NotBlank(message = "AddressType is Required")
    private AddressType addressType;

    private boolean defaultShipping;

    private boolean defaultBilling;

}
