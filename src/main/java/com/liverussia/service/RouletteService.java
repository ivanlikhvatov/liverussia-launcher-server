package com.liverussia.service;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.SpinRouletteResponseDto;

public interface RouletteService {
    SpinRouletteResponseDto spinRoulette(JwtUser jwtUser);
}
