package com.liverussia.service;

import com.liverussia.dto.response.LoaderSliderInfoResponseDto;
import com.liverussia.dto.response.ServerImagesResponseDto;

public interface AndroidSettingService {
    ServerImagesResponseDto getPossiblePrizesInfo();

    ServerImagesResponseDto getDonateServicesInfo();

    LoaderSliderInfoResponseDto getLoaderSliderInfo();
}
