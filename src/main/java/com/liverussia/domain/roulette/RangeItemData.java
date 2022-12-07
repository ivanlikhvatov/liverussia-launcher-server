package com.liverussia.domain.roulette;

import com.liverussia.dao.entity.roulette.rangeItem.RangeElementType;
import lombok.Data;

@Data
public class RangeItemData {
    private Long id;

    private Long rangeStart;

    private Long rangeEnd;

    private RangeElementType rangeElementType;

    private String prizeImageFileName;
}
