package com.liverussia.dao.entity.roulette.compositeItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompositeElementType {
    CAR("2"),
    MASK("6"),
    SKIN("5"),
    BACKPACK("6"),
    GUN("4"),
    CAP("6");

    private final String sampType;
}
