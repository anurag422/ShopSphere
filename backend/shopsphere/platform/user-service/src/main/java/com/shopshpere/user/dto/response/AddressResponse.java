package com.shopshpere.user.dto.response;

import com.shopshpere.user.Enum.AddressType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private AddressType addressType;

    private boolean defaultShipping;

    private boolean defaultBilling;

}
