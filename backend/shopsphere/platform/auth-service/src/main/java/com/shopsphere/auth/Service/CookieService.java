package com.shopsphere.auth.Service;

import com.shopsphere.auth.Entity.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {

    void addRefreshTokenCookie(HttpServletResponse response, String refreshToken);

    void deleteRefreshTokenCookie(HttpServletResponse response);

}
