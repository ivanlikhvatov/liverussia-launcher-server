package com.liverussia.service.impl;

import com.liverussia.domain.ServerImageInfo;
import com.liverussia.dto.response.ServerImagesResponseDto;
import com.liverussia.service.AndroidSettingService;
import com.liverussia.utils.Base64Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AndroidSettingServiceImpl implements AndroidSettingService {

    private final static String POSSIBLE_PRIZES_DIRECTORY = "possible_prizes/";
    private final static String DONATE_SERVICES_DIRECTORY = "donate_services/";

    private final List<ServerImageInfo> possiblePrizesInfo;
    private final List<ServerImageInfo> donateServicesInfo;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public ServerImagesResponseDto getPossiblePrizesInfo() {

        ServerImagesResponseDto possiblePrizesInfoResponseDto = new ServerImagesResponseDto();

        String path = uploadPath.concat(POSSIBLE_PRIZES_DIRECTORY);

        List<String> base64Images = Optional.ofNullable(possiblePrizesInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(prizeInfo -> path.concat(prizeInfo.getFileName()))
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());

        possiblePrizesInfoResponseDto.setBase64Images(base64Images);

        return possiblePrizesInfoResponseDto;
    }

    @Override
    public ServerImagesResponseDto getDonateServicesInfo() {
        ServerImagesResponseDto possiblePrizesInfoResponseDto = new ServerImagesResponseDto();

        String path = uploadPath.concat(DONATE_SERVICES_DIRECTORY);

        List<String> base64Images = Optional.ofNullable(donateServicesInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(prizeInfo -> path.concat(prizeInfo.getFileName()))
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());

        possiblePrizesInfoResponseDto.setBase64Images(base64Images);

        return possiblePrizesInfoResponseDto;
    }
}
