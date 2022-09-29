package com.liverussia.dao.entity.roulette.rangeItem;

import com.liverussia.dao.entity.roulette.singleItem.SingleElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RangeElementType {
    GAME_CURRENCY("0"),
    EXPERIENCE("1"),
    LIVE_COINS("7");

    private final String sampType;

    public static RangeElementType of(String value) {
        return Arrays.stream(values())
                .filter(type ->
                        type.getSampType().equals(value))
                .findFirst()
                .orElse(null);
    }
}
