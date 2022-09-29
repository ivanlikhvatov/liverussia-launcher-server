package com.liverussia.dao.entity.roulette.compositeItem;

import com.liverussia.dao.entity.roulette.rangeItem.RangeElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CompositeElementType {
    CAR("2"),
    MASK("14"),
    SKIN("5"),
    BACKPACK("15"),
    GUN("4"),
    CAP("16");

    private final String sampType;

    public static CompositeElementType of(String value) {
        return Arrays.stream(values())
                .filter(type ->
                        type.getSampType().equals(value))
                .findFirst()
                .orElse(null);
    }
}
