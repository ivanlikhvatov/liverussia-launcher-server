package com.liverussia.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String captchaToken;
}