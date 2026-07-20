package com.shopshpere.user.Service.ServiceImpl;

import com.shopshpere.user.Entity.UserProfile;
import com.shopshpere.user.Exception.ProfileAlreadyExistException;
import com.shopshpere.user.Exception.UserProfileNotFoundException;
import com.shopshpere.user.Mapper.ProfileMapper;
import com.shopshpere.user.Repository.UserProfileRepository;
import com.shopshpere.user.Service.CurrentUserService;
import com.shopshpere.user.Service.ProfileService;
import com.shopshpere.user.dto.request.CreateProfileRequest;
import com.shopshpere.user.dto.request.UpdateProfileRequest;
import com.shopshpere.user.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private CurrentUserService currentUserService;


    private final ProfileMapper profileMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public ProfileResponse createUserProfile(CreateProfileRequest request) {

        Long currentUserId = currentUserService.getCurrentUserId();

        if (userProfileRepository.existsByUserId(currentUserId)){
            throw new ProfileAlreadyExistException("User Profile Already Exist");
        }

        UserProfile entity = profileMapper.toEntity(request);
        entity.setId(currentUserId);

        UserProfile userProfile = userProfileRepository.save(entity);

        return profileMapper.toResponse(userProfile);

    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        Long currentUserId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository.findByUserId(currentUserId).orElseThrow(() -> new UserProfileNotFoundException("User Profile is Not found"));

        return profileMapper.toResponse(userProfile);

    }

    @Override
    public ProfileResponse updateMyProfile(UpdateProfileRequest request) {
        Long currentUserId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository.findByUserId(currentUserId).orElseThrow(() -> new UserProfileNotFoundException("Profile is not found for User"));

        profileMapper.updateProfile(request,userProfile);

        UserProfile saved = userProfileRepository.save(userProfile);

        return profileMapper.toResponse(saved);
    }
}
