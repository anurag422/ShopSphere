package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Enum.AccountStatus;
import com.shopsphere.auth.Enum.AuthenticationEventType;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.AuthenticationAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationAuditService authenticationAuditService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        String email ="";
        String name ="";
        String picture = "";


        String registrationId = request.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(request);

        if (registrationId.equals("google")){

             name = oAuth2User.getAttribute("name");
             email = oAuth2User.getAttribute("email");
             picture = oAuth2User.getAttribute("picture");

        } else if (registrationId.equals("github")) {

            name = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
            picture = oAuth2User.getAttribute("avatar_url");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            User user1 = new User();

            user1.setEmail(email);
            user1.setAccountStatus(AccountStatus.ACTIVE);
            user1.setEmailVerified(true);

            user1.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

            userRepository.save(user1);

            authenticationAuditService.log(user1,registrationId.equals("google") ? AuthenticationEventType.GOOGLE_LOGIN : AuthenticationEventType.GITHUB_LOGIN);

        }


        return oAuth2User;

    }

}
