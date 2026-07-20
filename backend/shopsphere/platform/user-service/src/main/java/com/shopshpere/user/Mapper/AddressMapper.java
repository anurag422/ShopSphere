package com.shopshpere.user.Mapper;

import com.shopshpere.user.Entity.Address;
import com.shopshpere.user.dto.request.CreateAddressRequest;
import com.shopshpere.user.dto.request.UpdateAddressRequest;
import com.shopshpere.user.dto.response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    Address toEntity(CreateAddressRequest request);

    AddressResponse toResponse(Address address);

    void updateAddress(UpdateAddressRequest request, @MappingTarget Address address);

    List<AddressResponse> toResponseList(List<Address> addresses);

}
