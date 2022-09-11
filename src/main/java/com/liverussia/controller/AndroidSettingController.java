package com.liverussia.controller;

import com.liverussia.dto.response.PossiblePrizesInfoResponseDto;
import com.liverussia.service.AndroidSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/android/setting")
public class AndroidSettingController {

    private final AndroidSettingService androidSettingService;

    @GetMapping("/prizes/info")
    public PossiblePrizesInfoResponseDto getPossiblePrizesInfo() {
        return androidSettingService.getPossiblePrizesInfo();
    }
}
