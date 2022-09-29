package com.liverussia.dao.entity.roulette.rangeItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RangeElementType {
    GAME_CURRENCY("0"),
    EXPERIENCE("1"),
    LIVE_COINS("7");

    private final String sampType;
}
