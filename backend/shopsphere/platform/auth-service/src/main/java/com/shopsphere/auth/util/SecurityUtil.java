package com.shopsphere.auth.util;

import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(){

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByEmail(principal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;

    }

}
