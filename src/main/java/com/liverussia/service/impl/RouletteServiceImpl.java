package com.liverussia.service.impl;

import com.liverussia.dao.entity.user.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.dto.response.UserInfoDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.service.RouletteService;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouletteServiceImpl implements RouletteService {

    private final UserService userService;

    @Value("${android.roulette.spinCost}")
    private long spinCost;


    @Override
    public SpinRouletteResponseDto spinRoulette(JwtUser jwtUser) {

        Optional.ofNullable(jwtUser)
                .orElseThrow(() -> new ApiException(ErrorContainer.AUTHENTICATION_ERROR));

        UserInfoDto user = userService.getUserInfo(jwtUser);
        checkUserBalance(user);

        //choose prize and build request logic


        return null;
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
}
