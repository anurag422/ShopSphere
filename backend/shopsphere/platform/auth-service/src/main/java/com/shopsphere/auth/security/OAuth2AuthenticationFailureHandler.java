package com.shopsphere.auth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        String errorCode = switch (exception) {
            case OAuth2AuthenticationException ex -> "oauth2_authentication_failed";
            case InternalAuthenticationServiceException ex -> "internal_error";
            default -> "authentication_failed";
        };

        getRedirectStrategy().sendRedirect(
                request,
                response,
                "http://localhost:3000/login?error=" + errorCode
        );

    }

}
