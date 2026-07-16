package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Service.RequestContextService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class RequestContextServiceImpl implements RequestContextService {
    @Override
    public String getClientIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestHeader = request.getHeader("X-Forwarded-for");

        if (requestHeader != null && !requestHeader.isBlank()){
            return requestHeader.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    @Override
    public String getUserAgent() {

        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        return request.getHeader("User-Agent");

    }
}
