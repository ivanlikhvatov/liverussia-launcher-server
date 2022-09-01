package com.liverussia.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminAuthenticationRequestDto {
    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
