package com.liverussia.service;

import com.liverussia.dto.request.AuthenticationRequestDto;
import com.liverussia.dto.response.AuthenticationResponseDto;

public interface AuthService {
    AuthenticationResponseDto login(AuthenticationRequestDto request);

    AuthenticationResponseDto getAccessToken(String refreshToken);

    AuthenticationResponseDto refreshToken(String refreshToken);
}
