package com.shopshpere.user.Service.ServiceImpl;

import com.shopshpere.user.Exception.UnauthorizedException;
import com.shopshpere.user.Service.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final HttpServletRequest request;


    @Override
    public Long getCurrentUserId() {
        String header = request.getHeader("X-User-Id");

        if (header == null){
            throw new UnauthorizedException("User is Not Authenticated");
        }

        return Long.valueOf(header);

    }

    @Override
    public String getCurrentUserEmail() {
        String header = request.getHeader("X-User-Email");

        if (header == null){
            throw new UnauthorizedException("User is not Authenticated");
        }

        return header;
    }
}
