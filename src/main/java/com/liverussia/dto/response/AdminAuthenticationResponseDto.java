package com.liverussia.dto.response;

import lombok.Data;

@Data
public class AdminAuthenticationResponseDto {

    private String accessToken;

    private String refreshToken;

    private String username;
}
