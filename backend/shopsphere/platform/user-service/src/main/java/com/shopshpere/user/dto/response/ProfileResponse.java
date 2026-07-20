package com.shopshpere.user.dto.response;

import com.shopshpere.user.Enum.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String profileImageUrl;

    private String bio;

}
