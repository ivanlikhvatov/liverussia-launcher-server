package com.liverussia.service.impl;

import com.liverussia.dao.entity.user.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.PrizeInfoResponseDto;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.dto.response.UserInfoDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.service.RouletteService;
import com.liverussia.service.UserService;
import com.liverussia.utils.Base64Converter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouletteServiceImpl implements RouletteService {

    //TODO вынести это в yml
    private final static String ROULETTE_ITEMS_DIRECTORY = "roulette_items/";
    private final static String PRIZES_INFO_DIRECTORY = "prizes_info/";

    private final UserService userService;

    @Value("${android.roulette.spinCost}")
    private Integer spinCost;

    @Value("${android.roulette.countElementsInOneSpin}")
    private Integer countElementsInOneSpin;

    @Value("${android.roulette.spinDurationInMillis}")
    private Long spinDurationInMillis;

    @Value("${upload.path}")
    private String uploadPath;


    @Override
    public SpinRouletteResponseDto spinRoulette(JwtUser jwtUser) {

        Optional.ofNullable(jwtUser)
                .orElseThrow(() -> new ApiException(ErrorContainer.AUTHENTICATION_ERROR));

        UserInfoDto user = userService.getUserInfo(jwtUser);
        checkUserBalance(user);

        //choose prize and build request logic

        return buildRouletteResponseDto(jwtUser);
    }

    private void checkUserBalance(UserInfoDto user) {
        if (StringUtils.isBlank(user.getBalance())) {
            throw new ApiException(ErrorContainer.NOT_ENOUGH_MONEY);
        }

        long balance = Long.parseLong(user.getBalance());

        if (balance < spinCost) {
            throw new ApiException(ErrorContainer.NOT_ENOUGH_MONEY);
        }
    }

    private SpinRouletteResponseDto buildRouletteResponseDto(JwtUser user) {
        SpinRouletteResponseDto responseDto = new SpinRouletteResponseDto();

        responseDto.setCountElementsInOneSpin(countElementsInOneSpin.toString());
        responseDto.setSpinDurationInMillis(spinDurationInMillis.toString());

        responseDto.setBase64Images(buildRouletteBase64Images());
        responseDto.setPrizeInfo(buildPrizeInfo());

        int currentBalance = Integer.parseInt(user.getBalance()) - spinCost;
        responseDto.setBalance(Integer.toString(currentBalance));

        return responseDto;
    }

    private List<String> buildRouletteBase64Images() {

        List<String> base64Images = new ArrayList<>();
        String path = uploadPath.concat(ROULETTE_ITEMS_DIRECTORY);

        for (int i = 0; i < countElementsInOneSpin/2; i++) {
            String base64Image1 = Base64Converter.encodeFileToBase64(path.concat("experience.png"));
            base64Images.add(base64Image1);
            String base64Image2 = Base64Converter.encodeFileToBase64(path.concat("bmw_m5.png"));
            base64Images.add(base64Image2);
        }

        return base64Images;
    }

    private PrizeInfoResponseDto buildPrizeInfo() {

        String path = uploadPath.concat(PRIZES_INFO_DIRECTORY).concat("experience.png");

        PrizeInfoResponseDto prizeInfoResponseDto = new PrizeInfoResponseDto();
        prizeInfoResponseDto.setBase64Image(Base64Converter.encodeFileToBase64(path));
        prizeInfoResponseDto.setAdditionalInfo("6 EXP");

        return prizeInfoResponseDto;
    }
}
