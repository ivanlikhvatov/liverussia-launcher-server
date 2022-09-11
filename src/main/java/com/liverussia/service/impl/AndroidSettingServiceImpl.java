package com.liverussia.service.impl;

import com.liverussia.domain.PossiblePrizeInfo;
import com.liverussia.dto.response.PossiblePrizesInfoResponseDto;
import com.liverussia.service.AndroidSettingService;
import com.liverussia.utils.Base64Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AndroidSettingServiceImpl implements AndroidSettingService {

    private final List<PossiblePrizeInfo> possiblePrizesInfo;

    @Override
    public PossiblePrizesInfoResponseDto getPossiblePrizesInfo() {

        PossiblePrizesInfoResponseDto possiblePrizesInfoResponseDto = new PossiblePrizesInfoResponseDto();

        List<String> base64Images = Optional.ofNullable(possiblePrizesInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(PossiblePrizeInfo::getFileName)
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());

        possiblePrizesInfoResponseDto.setBase64Images(base64Images);

        return possiblePrizesInfoResponseDto;
    }
}
