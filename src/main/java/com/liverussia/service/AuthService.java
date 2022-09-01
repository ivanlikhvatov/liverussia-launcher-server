package com.liverussia.service;

import com.liverussia.dto.request.AdminAuthenticationRequestDto;
import com.liverussia.dto.request.AuthenticationRequestDto;
import com.liverussia.dto.response.AdminAuthenticationResponseDto;
import com.liverussia.dto.response.AuthenticationResponseDto;

public interface AuthService {
    AuthenticationResponseDto loginAndroidUser(AuthenticationRequestDto request);

    AdminAuthenticationResponseDto loginAdminUser(AdminAuthenticationRequestDto request);

    AuthenticationResponseDto getAccessToken(String refreshToken);

    AuthenticationResponseDto refreshAndroidUserToken(String refreshToken);

    AdminAuthenticationResponseDto refreshAdminUserToken(String refreshToken);
}
