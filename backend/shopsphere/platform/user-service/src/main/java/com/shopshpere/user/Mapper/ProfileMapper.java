package com.shopshpere.user.Mapper;

import com.shopshpere.user.Entity.UserProfile;
import com.shopshpere.user.dto.request.CreateProfileRequest;
import com.shopshpere.user.dto.request.UpdateProfileRequest;
import com.shopshpere.user.dto.response.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    UserProfile toEntity(CreateProfileRequest request);

    ProfileResponse toResponse(UserProfile profile);

    void updateProfile(UpdateProfileRequest request, @MappingTarget UserProfile profile);

}
