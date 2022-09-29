package com.liverussia.dao.entity.roulette.singleItem;

import com.liverussia.error.apiException.ErrorContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SingleElementType {
    CAR("2", StringUtils.EMPTY),
    GOLDEN_VIP("8", "2"),
    SILVER_VIP("8", "1"),
    PLATINUM_VIP("8", "3"),
    REMOVE_ONE_WARNING("13", "1"),
    ADDITIONAL_CAR_SLOT("12", StringUtils.EMPTY),
    MILITARY_ID("9", StringUtils.EMPTY),
    ALL_LICENSES_PACKAGE("10", "2592000");

    private final String sampType;
    private final String sampValue;

    public static SingleElementType of(String value) {
        return Arrays.stream(values())
                .filter(type ->
                        type.getSampType().equals(value))
                .findFirst()
                .orElse(null);
    }
}
