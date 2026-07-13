package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.Role;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AccountStatus;
import com.shopsphere.auth.Enum.RoleType;
import com.shopsphere.auth.Exception.EmailAlreadyExistsException;
import com.shopsphere.auth.Exception.RoleNotFoundException;
import com.shopsphere.auth.Repository.RoleRepository;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.AuthService;
import com.shopsphere.auth.dto.request.RegisterRequest;
import com.shopsphere.auth.dto.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already Exist");
        }

        Role role =  roleRepository.findByName(RoleType.ROLE_CUSTOMER).orElseThrow(()->new RoleNotFoundException("ROLE_CUSTOMER"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(false)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        return RegisterResponse.builder().email(user.getEmail()).userId(user.getId()).message("Registered Successfully").build();

    }
}
