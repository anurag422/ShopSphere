package com.shopsphere.auth.Service.ServiceImpl;

import com.shopsphere.auth.Service.DeviceInfoService;
import com.shopsphere.auth.Service.RequestContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {

    @Autowired
    private RequestContextService requestContextService;

    @Override
    public String getDeviceName() {
        return requestContextService.getUserAgent();
    }
}
