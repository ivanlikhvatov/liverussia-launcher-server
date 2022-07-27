package com.liverussia.domain;

import lombok.Data;

@Data
public class TokenAndExpiration {

    private String token;

    private String expiration;
}
