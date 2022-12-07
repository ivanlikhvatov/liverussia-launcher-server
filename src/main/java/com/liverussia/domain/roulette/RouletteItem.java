package com.liverussia.domain.roulette;

import com.liverussia.dao.entity.roulette.item.CategoryEntity;
import com.liverussia.dao.entity.roulette.item.RouletteItemType;
import lombok.Data;

@Data
public class RouletteItem {
    private Long id;

    private String name;

    private CategoryEntity category;

    private String imageFileName;

    private byte[] imageFile;

    private RouletteItemType type;

    private SingleItemData singleItemData;

    private RangeItemData rangeItemData;

    private CompositeItemData compositeItemData;
}
