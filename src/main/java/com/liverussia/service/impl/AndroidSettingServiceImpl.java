package com.liverussia.service.impl;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeElementType;
import com.liverussia.dao.entity.roulette.rangeItem.RangeElementType;
import com.liverussia.dao.entity.roulette.singleItem.SingleElementType;
import com.liverussia.dao.entity.user.RoulettePrizes;
import com.liverussia.dao.repository.RoulettePrizesRepository;
import com.liverussia.domain.PossiblePrizeInfo;
import com.liverussia.dto.response.PossiblePrizesInfoResponseDto;
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

    private final List<PossiblePrizeInfo> possiblePrizesInfo;
    private final RoulettePrizesRepository roulettePrizesRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public PossiblePrizesInfoResponseDto getPossiblePrizesInfo() {

        PossiblePrizesInfoResponseDto possiblePrizesInfoResponseDto = new PossiblePrizesInfoResponseDto();

        String path = uploadPath.concat(POSSIBLE_PRIZES_DIRECTORY);

        List<String> base64Images = Optional.ofNullable(possiblePrizesInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(prizeInfo -> path.concat(prizeInfo.getFileName()))
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());

        possiblePrizesInfoResponseDto.setBase64Images(base64Images);


        List<RoulettePrizes> roulettePrizes = roulettePrizesRepository.findAll();


        Optional.ofNullable(roulettePrizes)
                .orElse(Collections.emptyList())
                .forEach(prize -> {
                    SingleElementType singleElementType = SingleElementType.of(prize.getType());
                    RangeElementType rangeElementType = RangeElementType.of(prize.getType());
                    CompositeElementType compositeElementType = CompositeElementType.of(prize.getType());

                    if (singleElementType != null) {
                        System.out.println("item type: " + singleElementType + "; " + "item value: " + prize.getValue());
                    }

                    if (rangeElementType != null) {
                        System.out.println("item type: " + rangeElementType + "; " + "item value: " + prize.getValue());
                    }

                    if (compositeElementType != null) {
                        System.out.println("item type: " + compositeElementType + "; " + "item value: " + prize.getValue());
                    }
                });


        return possiblePrizesInfoResponseDto;
    }
}
