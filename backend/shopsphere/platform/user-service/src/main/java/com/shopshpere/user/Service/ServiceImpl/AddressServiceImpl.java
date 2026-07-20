package com.shopshpere.user.Service.ServiceImpl;

import com.shopshpere.user.Entity.Address;
import com.shopshpere.user.Exception.AddressNotFoundException;
import com.shopshpere.user.Helper.HelperClass;
import com.shopshpere.user.Mapper.AddressMapper;
import com.shopshpere.user.Repository.AddressRepository;
import com.shopshpere.user.Service.AddressService;
import com.shopshpere.user.Service.CurrentUserService;
import com.shopshpere.user.dto.request.CreateAddressRequest;
import com.shopshpere.user.dto.request.UpdateAddressRequest;
import com.shopshpere.user.dto.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    @Autowired
    private CurrentUserService currentUserService;

    private final AddressMapper addressMapper;

    private final AddressRepository addressRepository;

    private final HelperClass helperClass;


    @Override
    public AddressResponse createAddress(CreateAddressRequest request) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressMapper.toEntity(request);

        address.setUserId(currentUserId);

        List<Address> addresses = addressRepository.findByUserId(currentUserId);

        if (addresses.isEmpty()){
            address.setDefaultBilling(true);
            address.setDefaultShipping(true);
        }else {
            if (request.isDefaultShipping()){

                addressRepository.findByUserIdAndDefaultShippingTrue(currentUserId).ifPresent(existing->{
                    existing.setDefaultShipping(false);
                    addressRepository.save(existing);
                });
            }

            if (request.isDefaultBilling()){

                addressRepository.findByUserIdAndDefaultBillingTrue(currentUserId).ifPresent(existing->{
                    existing.setDefaultBilling(false);
                    addressRepository.save(existing);
                });

            }
        }

        Address saved = addressRepository.save(address);

        return addressMapper.toResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddress() {

        Long currentUserId = currentUserService.getCurrentUserId();

        List<Address> addresses = addressRepository.findByUserId(currentUserId);

        return addressMapper.toResponseList(addresses);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getById(Long addressId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressRepository.findByIdAndUserId(addressId, currentUserId).orElseThrow(()->new AddressNotFoundException("Address not found"));

        return addressMapper.toResponse(address);
    }

    @Override
    public AddressResponse updateAddress(Long addressId, UpdateAddressRequest request) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressRepository.findByIdAndUserId(addressId, currentUserId).orElseThrow(()->new AddressNotFoundException("Address not found"));

        addressMapper.updateAddress(request,address);

         helperClass.handleDefaultShipping(currentUserId,address,request.isDefaultShipping());

        helperClass.handleDefaultBilling(currentUserId,address, request.isDefaultBilling());

        Address saved = addressRepository.save(address);

        return addressMapper.toResponse(saved);
    }

    @Override
    public void delete(Long addressId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressRepository.findByIdAndUserId(addressId, currentUserId).orElseThrow(()->new AddressNotFoundException("Address is not found"));

        boolean isShippingDefault = address.isDefaultShipping();

        boolean isBillingDefault = address.isDefaultBilling();

        addressRepository.delete(address);


        if (isShippingDefault){
            helperClass.assignNewDefaultShipping(currentUserId);
        }

        if (isBillingDefault){
            helperClass.assignNewDefaultBilling(currentUserId);
        }

    }

    @Override
    public void setDefaultShipping(Long addressId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressRepository.findByIdAndUserId(addressId, currentUserId).orElseThrow(() -> new AddressNotFoundException("Address Not Found"));

        helperClass.handleDefaultShipping(currentUserId,address,true);

        addressRepository.save(address);

    }

    @Override
    public void setDefaultBilling(Long addressId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        Address address = addressRepository.findByIdAndUserId(addressId, currentUserId).orElseThrow(() -> new AddressNotFoundException("Address is Not found"));

        helperClass.handleDefaultBilling(currentUserId,address,true);

        addressRepository.save(address);

    }


}
