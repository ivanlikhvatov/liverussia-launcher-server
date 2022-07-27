package com.liverussia.service;

import com.liverussia.dto.request.JwtRequest;
import com.liverussia.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest request);

    JwtResponse getAccessToken(String refreshToken);

    JwtResponse refresh(String refreshToken);
}
