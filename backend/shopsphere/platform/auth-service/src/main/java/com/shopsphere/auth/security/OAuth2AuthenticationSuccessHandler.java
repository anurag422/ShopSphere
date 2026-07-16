package com.shopsphere.auth.security;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Entity.User;
import com.shopsphere.auth.Repository.UserRepository;
import com.shopsphere.auth.Service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();


        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));


        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        getRedirectStrategy().sendRedirect(request,response,"http://localhost:3000/oauth2/success"
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken.getToken());

    }

}
