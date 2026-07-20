package com.shopshpere.user.Entity;

import com.shopshpere.user.Enum.AddressType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false,length = 100)
    private String fullName;

    @Column(nullable = false,length = 20)
    private String phoneNumber;

    @Column(nullable = false,length = 250)
    private String addressLine1;

    @Column(nullable = false,length = 250)
    private String addressLine2;

    @Column(nullable = false,length = 20)
    private String city;

    @Column(nullable = false,length = 20)
    private String state;

    @Column(nullable = false,length = 15)
    private String country;

    @Column(nullable = false,length = 20)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private boolean defaultShipping;

    private boolean defaultBilling;

}
