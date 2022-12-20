package com.liverussia.controller;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.dto.response.UserInfoDto;
import com.liverussia.service.RouletteService;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/android/user")
public class AndroidUserController {

    private final UserService userService;
    private final RouletteService rouletteService;

    @GetMapping("/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal JwtUser jwtUser) {
        return userService.getUserInfo(jwtUser);
    }

    @PostMapping("/roulette/spin")
    public SpinRouletteResponseDto spinRoulette(@AuthenticationPrincipal JwtUser jwtUser) {

        for (int i = 0; i < 1000; i++ ) {
            rouletteService.spinRoulette(jwtUser);
        }

        return rouletteService.spinRoulette(jwtUser);
    }
}
