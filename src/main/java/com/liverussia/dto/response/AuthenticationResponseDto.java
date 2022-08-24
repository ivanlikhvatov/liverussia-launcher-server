package com.liverussia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

//    private final String type = "Bearer";

    private String accessToken;

    private String refreshToken;

    private String balance;

    private String username;

    private String serverName;


}
