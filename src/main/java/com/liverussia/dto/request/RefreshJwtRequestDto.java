package com.liverussia.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshJwtRequestDto {

    @NotBlank
    public String refreshToken;
}
