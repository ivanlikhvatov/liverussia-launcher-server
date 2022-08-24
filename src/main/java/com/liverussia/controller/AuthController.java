package com.liverussia.controller;

import com.liverussia.dto.request.AuthenticationRequestDto;
import com.liverussia.dto.request.RefreshJwtRequestDto;
import com.liverussia.dto.response.AuthenticationResponseDto;
import com.liverussia.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthenticationResponseDto login(@RequestBody @Valid AuthenticationRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthenticationResponseDto getNewRefreshToken(@RequestBody @Valid RefreshJwtRequestDto request) {
        return authService.refresh(request.getRefreshToken());
    }

//    @PostMapping("/token")
//    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody @Valid RefreshJwtRequest request) {
//        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
//        return ResponseEntity.ok(token);
//    }

}
