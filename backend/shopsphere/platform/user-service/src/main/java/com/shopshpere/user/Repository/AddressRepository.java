package com.shopshpere.user.Repository;

import com.shopshpere.user.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findByUserId(Long id);

    Optional<Address> findByIdAndUserId(Long id,Long userId);

    Optional<Address> findByUserIdAndDefaultShippingTrue(Long userId);

    Optional<Address> findByUserIdAndDefaultBillingTrue(Long userId);

}
