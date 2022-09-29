package com.liverussia.dao.entity.roulette.compositeItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
