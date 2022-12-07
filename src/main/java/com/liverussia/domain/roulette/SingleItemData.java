package com.liverussia.domain.roulette;

import com.liverussia.dao.entity.roulette.singleItem.SingleElementType;
import lombok.Data;

@Data
public class SingleItemData {
    private Long id;

    private String sampElementId;

    private SingleElementType singleElementType;

    private String prizeImageFileName;
}
