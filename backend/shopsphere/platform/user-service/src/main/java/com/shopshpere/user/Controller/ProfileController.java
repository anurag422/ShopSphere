package com.shopshpere.user.Controller;

import com.shopshpere.user.Service.ProfileService;
import com.shopshpere.user.dto.request.CreateProfileRequest;
import com.shopshpere.user.dto.request.UpdateProfileRequest;
import com.shopshpere.user.dto.response.ProfileResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody CreateProfileRequest request){
        ProfileResponse userProfile = profileService.createUserProfile(request);

        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(){
        ProfileResponse myProfile = profileService.getMyProfile();

        return ResponseEntity.ok(myProfile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request){
        ProfileResponse profileResponse = profileService.updateMyProfile(request);

        return ResponseEntity.ok(profileResponse);
    }

}
