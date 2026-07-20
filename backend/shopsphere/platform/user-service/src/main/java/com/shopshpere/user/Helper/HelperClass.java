package com.shopshpere.user.Helper;

import com.shopshpere.user.Entity.Address;
import com.shopshpere.user.Repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperClass {

    private final AddressRepository addressRepository;

    public void handleDefaultShipping(Long id, Address address, boolean makeDefault){

        if (!makeDefault){
            return;
        }

        addressRepository.findByUserIdAndDefaultShippingTrue(id).ifPresent(existing->{
            if (!existing.getId().equals(address.getId())){
                existing.setDefaultShipping(false);

                addressRepository.save(existing);
            }
        });

        address.setDefaultShipping(true);

    }

    public void handleDefaultBilling(Long id,Address address,boolean makeDefault){

        if (!makeDefault){
            return;
        }

        addressRepository.findByUserIdAndDefaultBillingTrue(id).ifPresent(existing->{
            if (!existing.getId().equals(address.getId())){
                existing.setDefaultBilling(false);

                addressRepository.save(existing);
            }
        });

        address.setDefaultBilling(true);

    }

    public void assignNewDefaultShipping(Long userId){

        addressRepository.findByUserId(userId).stream().findFirst().ifPresent(existing->{
            existing.setDefaultShipping(true);

            addressRepository.save(existing);
        });

    }

    public void assignNewDefaultBilling(Long userId){

        addressRepository.findByUserId(userId).stream().findFirst().ifPresent(existing->{
            existing.setDefaultBilling(true);

            addressRepository.save(existing);
        });

    }

}
