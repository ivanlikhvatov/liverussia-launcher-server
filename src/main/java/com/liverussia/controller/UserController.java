package com.liverussia.controller;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.UserInfoDto;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal JwtUser jwtUser) {
        return userService.getUserInfo(jwtUser);
    }
}
