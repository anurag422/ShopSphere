package com.shopsphere.auth.Service;

import com.shopsphere.auth.dto.request.RegisterRequest;
import com.shopsphere.auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

}
