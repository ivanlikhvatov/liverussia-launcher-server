package com.liverussia.mapper;

import com.liverussia.dao.entity.roulette.item.RouletteItemEntity;
import com.liverussia.domain.roulette.RouletteItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RouletteItemMapper {
    RouletteItem map(RouletteItemEntity entity);

    List<RouletteItem> map(List<RouletteItemEntity> entities);
}
