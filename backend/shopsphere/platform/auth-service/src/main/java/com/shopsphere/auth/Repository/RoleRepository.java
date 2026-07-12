package com.shopsphere.auth.Repository;

import com.shopsphere.auth.Entity.Role;
import com.shopsphere.auth.Enum.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleType roleType);

}
