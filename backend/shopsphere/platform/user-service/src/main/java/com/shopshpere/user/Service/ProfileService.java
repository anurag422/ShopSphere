package com.shopshpere.user.Service;

import com.shopshpere.user.dto.request.CreateProfileRequest;
import com.shopshpere.user.dto.request.UpdateProfileRequest;
import com.shopshpere.user.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse createUserProfile(CreateProfileRequest request);

    ProfileResponse getMyProfile();

    ProfileResponse updateMyProfile(UpdateProfileRequest request);

}
