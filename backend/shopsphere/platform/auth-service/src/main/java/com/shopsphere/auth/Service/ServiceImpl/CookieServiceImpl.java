package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Entity.RefreshToken;
import com.shopsphere.auth.Service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie("refreshToken",refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7*24*60*60);

        response.addCookie(cookie);

    }

    @Override
    public void deleteRefreshTokenCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken","");

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

    }
}
