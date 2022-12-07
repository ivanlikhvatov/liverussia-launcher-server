package com.liverussia.domain.roulette;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeElementType;
import lombok.Data;

@Data
public class CompositeItemData {
    private Long id;

    private CompositeElementType type;
}
