package com.liverussia.controller;

import com.liverussia.dto.response.LoaderSliderInfoResponseDto;
import com.liverussia.dto.response.ServerImagesResponseDto;
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
    public ServerImagesResponseDto getPossiblePrizesInfo() {
        return androidSettingService.getPossiblePrizesInfo();
    }

    @GetMapping("/donate/services")
    public ServerImagesResponseDto getDonateServicesInfo() {
        return androidSettingService.getDonateServicesInfo();
    }

    @GetMapping("/loader/slider/info")
    public LoaderSliderInfoResponseDto getLoaderSliderInfo() {
        return androidSettingService.getLoaderSliderInfo();
    }
}
