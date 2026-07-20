package com.shopshpere.user.dto.request;

import com.shopshpere.user.Enum.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfileRequest {

    @NotBlank(message = "First name is Required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number.")
    private String phoneNumber;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Past(message = "Birth date Cannot be in future")
    private LocalDate dateOfBirth;

    @Size(max = 1000)
    private String bio;

}
