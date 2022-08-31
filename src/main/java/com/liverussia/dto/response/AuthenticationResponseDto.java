package com.liverussia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    private String accessToken;

    private String refreshToken;

    private UserInfoDto userInfo;
}
