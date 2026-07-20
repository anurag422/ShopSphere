package com.shopshpere.user.Service;

import com.shopshpere.user.dto.request.CreateAddressRequest;
import com.shopshpere.user.dto.request.UpdateAddressRequest;
import com.shopshpere.user.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(CreateAddressRequest request);

    List<AddressResponse> getAddress();

    AddressResponse getById(Long addressId);

    AddressResponse updateAddress(Long addressId,UpdateAddressRequest request);

    void delete(Long addressId);

    void setDefaultShipping(Long addressId);

    void setDefaultBilling(Long addressId);

}
